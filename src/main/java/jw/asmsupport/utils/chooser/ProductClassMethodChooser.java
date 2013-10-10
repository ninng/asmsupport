/**
 * 
 */
package jw.asmsupport.utils.chooser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import jw.asmsupport.clazz.AClass;
import jw.asmsupport.clazz.ProductClass;
import jw.asmsupport.definition.method.Method;
import jw.asmsupport.entity.MethodEntity;
import jw.asmsupport.exception.ASMSupportException;
import jw.asmsupport.utils.AClassUtils;
import jw.asmsupport.utils.ASConstant;


/**
 * 
 * @author 温斯群(Joe Wen)
 *
 */
public class ProductClassMethodChooser extends AbstractMethodChooser {
    
    private ProductClass methodOwner;
    
    public ProductClassMethodChooser(AClass invoker, ProductClass methodOwner, String name, AClass[] argumentTypes) {
        super(invoker, name, argumentTypes);
        this.methodOwner = methodOwner;
    }


    @Override
    public MethodEntity firstPhase() {
    	MethodEntity me4ReallyClass = determineMostSpecificMethodEntity(determineMethodInJavaClass(invoker, methodOwner.getReallyClass(), name, argumentTypes, allArgTypes));
    	
    	if(!CollectionUtils.isEmpty(methodOwner.getMethods())){
    		//find in current class
            List<Method> methods = new ArrayList<Method>(methodOwner.getMethods());
            //Collections.copy(methods, methodOwner.getMethods());
            TypeTreeNode[] ttns;

            //所有找到的可能的方法
            List<MethodEntityTypeTreeNodeCombine> foundCombine = new ArrayList<MethodEntityTypeTreeNodeCombine>();
            //参数是否和传入的参数相同
            boolean sameToPass = true;
            MethodEntity mte;
            for(int i=0, length = allArgTypes.size(); i<length; i++){
                ttns = allArgTypes.get(i);
                for(int k=0, mlen = methods.size();  k<mlen; k++){
                    mte = methods.get(k).getMethodEntity();
                    //如果名字不相同直接跳过
                    if(!name.equals(mte.getName())){
                        continue;
                    }
                    
                    AClass[] mtdArgs = mte.getArgClasses();
                    //如果参数个数不同直接跳过
                    if(mtdArgs.length != ttns.length){
                        continue;
                    }
                    //如果有一个参数的类型不同直接跳过
                    for(int j=0; j<ttns.length; j++){
                        if(!ttns[j].type.equals(mtdArgs[j])){
                            sameToPass = false;
                            continue;
                        }
                    }
                    
                    if(i == 0 && sameToPass){
                        return mte;
                    }
                    methods.remove(k);
                    foundCombine.add(new MethodEntityTypeTreeNodeCombine(mte, allArgTypes.remove(i)));
                    length--;
                    break;
                }
            }
            
            MethodEntityTypeTreeNodeCombine[] mettncs = foundCombine.toArray(new MethodEntityTypeTreeNodeCombine[foundCombine.size()]);
            
            MethodEntity me4newCreateMethod = determineMostSpecificMethodEntity(mettncs);
            
            if(me4ReallyClass == null && me4newCreateMethod != null){
            	return me4newCreateMethod;
            }else if(me4ReallyClass != null && me4newCreateMethod == null){
            	return me4ReallyClass;
            }else{
            	return null;
            }
    	}else{
    		return me4ReallyClass;
    	}
    	
    	//return determineMostSpecificMethodEntity(determineMethodInJavaClass(invoker, methodOwner.getReallyClass(), name, argumentTypes));
    }

    @Override
    public MethodEntity secondPhase() {
        List<AClass[]> list = new ArrayList<AClass[]>(AClassUtils.allArgumentWithBoxAndUnBoxCountExceptSelf(argumentTypes));
        AClassUtils.allArgumentWithBoxAndUnBox(argumentTypes, AClassUtils.primitiveFlag(argumentTypes), 0, new AClass[argumentTypes.length], list);
        
        int foundNumber = 0;
        MethodEntity me = null;
        //MethodEntityTypeTreeNodeCombine[] mettnc;
        for(AClass[] argsTypes : list){
        	ProductClassMethodChooser pcmc = new ProductClassMethodChooser(invoker, methodOwner, name, argsTypes);
            me = pcmc.firstPhase();
            //mettnc = determineMethodInJavaClass(invoker, methodOwner.getReallyClass(), name, argsTypes);
            //me = determineMostSpecificMethodEntity(mettnc);
        	if(me != null){
                foundNumber++;
                if(foundNumber > 1){
                    throw new ASMSupportException(" Ambiguous ...............");
                }
            }
        }
        
        return me;
    }

    @Override
    public MethodEntity thirdPhase() {
        List<MethodEntity> applicable = applicableVariableVarifyMethod(invoker, methodOwner, name, argumentTypes);
        List<TypeTreeNode[]> appliNodes = applicableVariableVarifyMethodArgumentsNodes(applicable);

        if(appliNodes.size() > 0){
            int mostIndex = mostSpecificIndexForVariableVarify(appliNodes);
            if(mostIndex == -1){
                throw new ASMSupportException(" Ambiguous ...............");
            }else{
                MethodEntity me = applicable.get(mostIndex);
                return me;
            }
        }else{
            return null;
        }
    }


    @Override
    protected MethodEntity foundMethodWithNoArguments() {
    	for(Method m : methodOwner.getMethods()){
    		MethodEntity me = m.getMethodEntity();
    		if(m.getMode() == ASConstant.METHOD_CREATE_MODE_ADD &&
    		   me.getName().equals(this.name) &&
    		   me.getArgClasses().length == 0){
    			return me;
    		}
    	}
    	
        return foundMethodWithNoArguments(methodOwner.getReallyClass());
    }

}