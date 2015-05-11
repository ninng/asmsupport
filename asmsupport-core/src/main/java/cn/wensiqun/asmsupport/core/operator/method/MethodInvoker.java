/**    
 *  Asmsupport is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cn.wensiqun.asmsupport.core.operator.method;

import java.util.ArrayList;
import java.util.List;

import cn.wensiqun.asmsupport.core.asm.InstructionHelper;
import cn.wensiqun.asmsupport.core.block.KernelProgramBlock;
import cn.wensiqun.asmsupport.core.clazz.AClassFactory;
import cn.wensiqun.asmsupport.core.clazz.ArrayClass;
import cn.wensiqun.asmsupport.core.definition.KernelParam;
import cn.wensiqun.asmsupport.core.definition.method.AMethod;
import cn.wensiqun.asmsupport.core.definition.method.meta.AMethodMeta;
import cn.wensiqun.asmsupport.core.definition.variable.IVariable;
import cn.wensiqun.asmsupport.core.exception.NoSuchMethod;
import cn.wensiqun.asmsupport.core.log.Log;
import cn.wensiqun.asmsupport.core.log.LogFactory;
import cn.wensiqun.asmsupport.core.operator.AbstractParamOperator;
import cn.wensiqun.asmsupport.core.operator.Operator;
import cn.wensiqun.asmsupport.core.operator.array.KernelArrayValue;
import cn.wensiqun.asmsupport.core.utils.AClassUtils;
import cn.wensiqun.asmsupport.core.utils.ASConstant;
import cn.wensiqun.asmsupport.core.utils.jls15_12_2.MethodChooser;
import cn.wensiqun.asmsupport.core.utils.lang.ArrayUtils;
import cn.wensiqun.asmsupport.core.utils.reflect.ModifierUtils;
import cn.wensiqun.asmsupport.org.objectweb.asm.Type;
import cn.wensiqun.asmsupport.standard.def.clazz.AClass;
import cn.wensiqun.asmsupport.standard.error.ASMSupportException;

public abstract class MethodInvoker extends AbstractParamOperator {

    private static final Log LOG = LogFactory.getLog(MethodInvoker.class);

    protected String name;
    protected KernelParam[] arguments;
    protected AClass methodOwner;
    
    /** whether or not save the return value to stack, must be save a 
     * result to stack when call constructor*/
    private boolean saveReturn;
    
    /** found method entity will be called*/
    protected AMethodMeta mtdEntity;
    
    /**
     * 
     * @param owner
     * @param name
     * @param arguments
     * @param block
     */
    protected MethodInvoker(KernelProgramBlock block, AClass owner, String name, KernelParam[] arguments) {
        super(block, Operator.COMMON);
        this.methodOwner = owner;
        this.name = name;
        this.arguments = arguments;
    }

    protected void argumentsToStack() {
    	for(int i=0; i<arguments.length; i++){
            KernelParam argu = arguments[i];
            if(LOG.isPrintEnabled()) {
                LOG.print("push argument to stack");
            }
            if(argu instanceof IVariable){
                ((IVariable) argu).availableFor(this);
            }
            argu.loadToStack(block);
            cast(argu.getResultType(), mtdEntity.getArgClasses()[i]);
        }
    }
    
    private void cast(AClass from, AClass to){
        if(from.isPrimitive() && to.isPrimitive()){
            insnHelper.cast(from.getType(), to.getType());
        }else if(from.isPrimitive()){
            insnHelper.box(from.getType());
        }else if(AClassUtils.isPrimitiveWrapAClass(from) && to.isPrimitive()){
            Type primType = InstructionHelper.getUnBoxedType(from.getType());
            insnHelper.unbox(from.getType());
            insnHelper.cast(primType, to.getType());
        }
    }

    @Override
    protected void initAdditionalProperties() {
    	AClass[] argumentClasses = new AClass[arguments.length];
        List<AClass> argumentClassList = new ArrayList<AClass>();
        for (int i = 0; i < arguments.length; i++) {
            argumentClassList.add(arguments[i].getResultType());
        }
        argumentClassList.toArray(argumentClasses);
        
    	AMethod currentMethod = block.getMethod();
        if(currentMethod.getMode() == ASConstant.METHOD_CREATE_MODE_MODIFY && name.endsWith(ASConstant.METHOD_PROXY_SUFFIX)){
        	mtdEntity = (AMethodMeta) currentMethod.getMeta().clone();
            mtdEntity.setName(name);
        }else{
            mtdEntity = new MethodChooser(block.getMethodDeclaringClass(), methodOwner, name, argumentClasses).chooseMethod();
            if(mtdEntity == null){
                throw new NoSuchMethod(methodOwner, name, argumentClasses);
            }
        }
        
        if(ModifierUtils.isVarargs(mtdEntity.getModifier())){
            
        	AClass[] foundMethodArgTypes = mtdEntity.getArgClasses();
        	
        	if(ArrayUtils.getLength(foundMethodArgTypes) != ArrayUtils.getLength(arguments) ||
        	   !arguments[ArrayUtils.getLength(arguments) - 1].getResultType().isArray()){
        		
        		int fixedArgsLen = mtdEntity.getArgClasses().length - 1;
                KernelParam[] fixedArgs = new KernelParam[fixedArgsLen];
                System.arraycopy(arguments, 0, fixedArgs, 0, fixedArgsLen);

                KernelArrayValue variableVarifyArauments;
                ArrayClass arrayClass = (ArrayClass)mtdEntity.getArgClasses()[mtdEntity.getArgClasses().length - 1];
                variableVarifyArauments = block.newarray(arrayClass, 
                       (KernelParam[]) ArrayUtils.subarray(arguments, fixedArgsLen , arguments.length));
                variableVarifyArauments.asArgument();
                
                arguments = (KernelParam[]) ArrayUtils.add(fixedArgs, variableVarifyArauments);
        	}
        }

        for(AClass exception : mtdEntity.getExceptions()){
    	    block.addException(exception);
    	}
        
    }

    @Override
    protected void checkAsArgument() {
        for (KernelParam argu : arguments) {
            argu.asArgument();
        }
    }

    public boolean isSaveReference() {
        return saveReturn;
    }
    
    @Override
    public void asArgument() {
        block.removeExe(this);
    }

    @Override
    public void loadToStack(KernelProgramBlock block) {
        if(getReturnType().equals(Type.VOID_TYPE)){
            throw new ASMSupportException("cannot push the void return type to stack!");
        }
        
        boolean saveRef = isSaveReference();
        setSaveReference(true);
        this.execute();
        setSaveReference(saveRef);
    }

    protected void setSaveReference(boolean saveReturn) {
        this.saveReturn = saveReturn;
    }
    
    public Type getReturnType() {
        return getReturnClass() != null ? getReturnClass().getType() : Type.VOID_TYPE;
    }

    public final AClass getReturnClass() {
        if(name.equals(ASConstant.INIT)){
            return methodOwner;
        }else if(mtdEntity != null){
            return mtdEntity.getReturnClass();
        }else{
            return AClassFactory.getType(void.class);
        }
    }
    
    protected AClass getActuallyOwner(){
        return mtdEntity.getActuallyOwner();
    }
    
    /**
     * 
     * @return
     */
    public int getModifiers() {
        return this.mtdEntity.getModifier();
    }

    @Override
    public AClass getResultType() {
        return getReturnClass();
    }
    
}
