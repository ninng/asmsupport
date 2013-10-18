/**
 * 
 */
package cn.wensiqun.asmsupport.utils;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.objectweb.asm.Opcodes;

import cn.wensiqun.asmsupport.clazz.AClass;
import cn.wensiqun.asmsupport.clazz.AClassFactory;
import cn.wensiqun.asmsupport.clazz.ArrayClass;
import cn.wensiqun.asmsupport.clazz.ProductClass;
import cn.wensiqun.asmsupport.clazz.SemiClass;
import cn.wensiqun.asmsupport.definition.method.Method;
import cn.wensiqun.asmsupport.entity.MethodEntity;

/**
 * 
 * @author 温斯群(Joe Wen)
 *
 */
public class AClassUtils {
    /**
     * this class is a tools class. so don't support constructor
     */
    private AClassUtils(){
        throw new UnsupportedOperationException("cannot support new instance the utils class");
    }
    
    public static boolean isPrimitiveWrapAClass(AClass aclass){
        if(aclass.getName().equals(Byte.class.getName()) ||
           aclass.getName().equals(Short.class.getName()) ||
           aclass.getName().equals(Character.class.getName()) ||
           aclass.getName().equals(Integer.class.getName()) ||
           aclass.getName().equals(Long.class.getName()) ||
           aclass.getName().equals(Float.class.getName()) ||
           aclass.getName().equals(Double.class.getName()) ||
           aclass.getName().equals(Boolean.class.getName())){
            return true;
        }
        return false;
    }
    
    public static boolean arithmetical(AClass aclass){
        if(aclass.isPrimitive() && !aclass.getName().equals(boolean.class.getName())){
            return true;
        }else if(isPrimitiveWrapAClass(aclass) && !aclass.getName().equals(Boolean.class.getName())){
            return true;
        }
        return false;
    }
    
    public static boolean canUnboxOrBox(AClass aclass){
        if(aclass.isPrimitive() || isPrimitiveWrapAClass(aclass)){
            return true;
        }
        return false;
    }
    
    public static AClass getPrimitiveAClass(AClass aclass){
        if(aclass.equals(AClass.BOOLEAN_WRAP_ACLASS)){
            return AClass.BOOLEAN_ACLASS;
        }else if(aclass.equals(AClass.BYTE_WRAP_ACLASS)){
            return AClass.BYTE_ACLASS;
        }else if(aclass.equals(AClass.SHORT_WRAP_ACLASS)){
            return AClass.SHORT_ACLASS;
        }else if(aclass.equals(AClass.CHARACTER_WRAP_ACLASS)){
            return AClass.CHAR_ACLASS;
        }else if(aclass.equals(AClass.INTEGER_WRAP_ACLASS)){
            return AClass.INT_ACLASS;
        }else if(aclass.equals(AClass.LONG_WRAP_ACLASS)){
            return AClass.LONG_ACLASS;
        }else if(aclass.equals(AClass.FLOAT_WRAP_ACLASS)){
            return AClass.FLOAT_ACLASS;
        }else if(aclass.equals(AClass.DOUBLE_WRAP_ACLASS)){
            return AClass.DOUBLE_ACLASS;
        }
        return aclass;
    }
    
    public static AClass getPrimitiveWrapAClass(AClass aclass){
        if(aclass.equals(AClass.BOOLEAN_ACLASS)){
            return AClass.BOOLEAN_WRAP_ACLASS;
        }else if(aclass.equals(AClass.BYTE_ACLASS)){
            return AClass.BYTE_WRAP_ACLASS;
        }else if(aclass.equals(AClass.SHORT_ACLASS)){
            return AClass.SHORT_WRAP_ACLASS;
        }else if(aclass.equals(AClass.CHAR_ACLASS)){
            return AClass.CHARACTER_WRAP_ACLASS;
        }else if(aclass.equals(AClass.INT_ACLASS)){
            return AClass.INTEGER_WRAP_ACLASS;
        }else if(aclass.equals(AClass.LONG_ACLASS)){
            return AClass.LONG_WRAP_ACLASS;
        }else if(aclass.equals(AClass.FLOAT_ACLASS)){
            return AClass.FLOAT_WRAP_ACLASS;
        }else if(aclass.equals(AClass.DOUBLE_ACLASS)){
            return AClass.DOUBLE_WRAP_ACLASS;
        }
        return aclass;
    }
    
    /**
     * 判断是否可见
     * @param invoker 调用者所在的类
     * @param invoked 被调用的方法或者field所在的类
     * @param actuallyInvoked 被调用的方法或者field实际所在的类 actuallyInvoked必须是invoked或是其父类
     * @param mod 被调用的方法或者field的修饰符
     * @return
     */
    public static boolean visible(AClass invoker, AClass invoked, AClass actuallyInvoked, int mod){
        //只要是public就可见
        if(Modifier.isPublic(mod)){
            return true;
        }
        
        //如果invoked和 actuallyInvoked相同
        if(invoked.equals(actuallyInvoked)){
            //如果invoker和invoked相同
            if(invoker.equals(invoked)){
                //在同一个类中允许调用
                return true;
            }else{
                if(Modifier.isPrivate(mod)){
                    return false;
                }else{
                    if(invoker.getPackage().equals(invoked.getPackage())){
                        return true;
                    }else{
                        //如果是保护类型
                        if(Modifier.isProtected(mod)){
                            //如果是子类
                            if(invoker.isChildOrEqual(invoked)){
                                return true;
                            }
                        }
                    }
                }
            }
        }else{
            //先判断actuallyInvoked对invoked的可见性
            if(Modifier.isPrivate(mod)){
                return false;
            }
            
            //如果都在同一包下
            if(invoker.getPackage().equals(invoked.getPackage()) && 
               invoker.getPackage().equals(actuallyInvoked.getPackage())){
                return true;
            }
            
            if(Modifier.isProtected(mod)){
                if(invoker.isChildOrEqual(invoked) &&
                   invoked.isChildOrEqual(actuallyInvoked)){
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * get direct super type
     * @param as
     * @return
     */
    public static AClass[] getDirectSuperType(AClass as){
        AClass[] a = null;
        
        if (as.isPrimitive()) {
            if (as.equals(AClass.BYTE_ACLASS)) {
                a = new AClass[]{AClass.SHORT_ACLASS};
                
            } else if (as.equals(AClass.SHORT_ACLASS)) {
                a = new AClass[]{AClass.INT_ACLASS};
                
            } else if (as.equals(AClass.CHAR_ACLASS)) {
                a = new AClass[]{AClass.INT_ACLASS};
                
            }  else if (as.equals(AClass.INT_ACLASS)) {
                a = new AClass[]{AClass.LONG_ACLASS};
                
            } else if (as.equals(AClass.LONG_ACLASS)) {
                a = new AClass[]{AClass.FLOAT_ACLASS};
                
            } else if (as.equals(AClass.FLOAT_ACLASS)) {
                a = new AClass[]{AClass.DOUBLE_ACLASS};   
            }
            
        } else if (as.equals(AClass.OBJECT_ACLASS)) {
            
        } else if(as.isInterface()){
            Class<?>[] intfacs = as.getInterfaces();
            if(intfacs != null && intfacs.length > 0){
                a = new AClass[intfacs.length];
                for(int i=0; i<a.length; i++){
                    a[i] = AClassFactory.getProductClass(intfacs[i]);
                }
            }else{
                a = new AClass[]{AClass.OBJECT_ACLASS};
            }
        } else if (as.isArray()) {
            ArrayClass ac = (ArrayClass) as;
            AClass rootType = ac.getRootComponentClass();
            
            if(rootType.isPrimitive()){
                a = new AClass[2];
                a[0] = AClass.CLONEABLE_ACLASS;
                a[1] = AClass.SERIALIZABLE_ACLASS;
            }else{
                AClass[] rootSupers = getDirectSuperType(rootType);
                if(rootSupers != null){
                    a = new AClass[rootSupers.length];
                    for(int i=0; i<a.length; i++){
                        a[i] = AClassFactory.getArrayClass(rootSupers[i], ac.getDimension());
                    }
                }else{
                    a = new AClass[2];
                    a[0] = AClass.CLONEABLE_ACLASS;
                    a[1] = AClass.SERIALIZABLE_ACLASS;
                }
            }
        } else {
            Class<?> sup = as.getSuperClass();
            Class<?>[] intefaces = as.getInterfaces();
            
            a = new AClass[intefaces.length + 1];
            a[0] = AClassFactory.getProductClass(sup);
            
            for(int i = 1; i<a.length; i++){
                a[i] = AClassFactory.getProductClass(intefaces[i - 1]);
            }
            
        }
        
        return a;
    }
    
    public static boolean isSubOrEqualType(AClass subtype, AClass exceptSupertype){
    	if(subtype.equals(exceptSupertype)){
    		return true;
    	}
    	AClass[] actuallySupertypes = getDirectSuperType(subtype);
    	if(ArrayUtils.isNotEmpty(actuallySupertypes)){
    		for(AClass actual : actuallySupertypes){
    			if(actual.equals(exceptSupertype)){
    				return true;
    			}else{
    				if(isSubOrEqualType(actual, exceptSupertype)){
    					return true;
    				}
    			}
    		}
    	}
    	return false;
    }
    
    /**
     * 
     * @param invoker
     * @param owner
     * @param name
     * @param actualArgLength
     * @return
     */
    public static List<MethodEntity> allDeclareVariableArityMethod(AClass invoker, AClass owner, String name, int actualArgLength){
        List<MethodEntity> list = new ArrayList<MethodEntity>();
        Class<?> reallyClass = null;
        if(owner instanceof SemiClass){
            for(Method method : ((SemiClass)owner).getMethods()){
                if((method.getMethodEntity().getModifier() & Opcodes.ACC_VARARGS) != 0 && 
                    method.getMethodEntity().getName().equals(name)){
                    list.add(method.getMethodEntity());
                }
            }
            reallyClass = owner.getSuperClass();
        }else if (owner instanceof ProductClass) {
            reallyClass = ((ProductClass) owner).getReallyClass();
        }
        
        Class<?> actuallyMethodOwner = reallyClass;
        AClass invoked = AClassFactory.getProductClass(reallyClass);
        //ACC_VARARGS
        List<MethodEntity> methods = new ArrayList<MethodEntity>();
        java.lang.reflect.Method[] mes;
        for (; actuallyMethodOwner != null; actuallyMethodOwner = actuallyMethodOwner.getSuperclass()) {
            mes = actuallyMethodOwner.getDeclaredMethods();
            for(int i=0; i<mes.length; i++){
                if(mes[i].getName().equals(name) && (mes[i].getModifiers() & Opcodes.ACC_VARARGS) != 0){
                    methods.add(MethodEntity.methodToMethodEntity(invoked, mes[i]));
                }
            }
            addAndEliminateDupVariableArityMethod(invoker, invoked, name, actualArgLength, list, methods);
        }
        
        return list;
    }
    
    /**
     * 
     * @param invoker
     * @param invoked
     * @param name
     * @param actualArgLength
     * @param list
     * @param methods
     */
    private static void addAndEliminateDupVariableArityMethod(AClass invoker, AClass invoked, String name, int actualArgLength, List<MethodEntity> list, List<MethodEntity> methods){
        boolean same;
        int length = list.size();
        for(MethodEntity m1 : methods){
            same = false;
            for(int i=0; i < length ; i++){
                if(MethodUtils.methodEqualInHierarchy(m1, list.get(i))){
                    same = true;
                    break;
                }
            }
            
            if(!same && 
               ((m1.getModifier() & Opcodes.ACC_VARARGS) != 0) &&
               m1.getName().equals(name)){
                
                if(AClassUtils.visible(invoker, invoked, m1.getActuallyOwner(),  m1.getModifier()) &&
                   m1.getArgClasses().length <= actualArgLength + 1){
                    list.add(m1);
                }
            }
        }
    }
    
    /**
     * 
     * @param aclasses
     * @return
     */
    public static int allArgumentWithBoxAndUnBoxCountExceptSelf(AClass[] aclasses){
        int size = 0;
        for(AClass a : aclasses){
            if(AClassUtils.canUnboxOrBox(a)){
                if(size == 0){
                    size = 1;
                }
                size = size << 1;
            }
        }
        return size == 0 ? 0 : size - 1;
    }
    
    public static int primitiveFlag(AClass[] aclasses){
        int flagVal = 0;
        if(aclasses == null){
            return flagVal;
        }
        
        for(int i=0; i<aclasses.length; i++){
            if(aclasses[i].isPrimitive()){
                if(i == 0){
                    flagVal++;
                }else{
                    flagVal += 2 << (i - 1);
                }
            }
        }
        return flagVal;
    }
    
    public static void allArgumentWithBoxAndUnBox(AClass[] orgi, int orgiFlagValue, int index, AClass[] newa, List<AClass[]> list){

        newa[index] = AClassUtils.getPrimitiveAClass(orgi[index]);
        if(index == orgi.length - 1){
            if(AClassUtils.canUnboxOrBox(orgi[index])){
                AClass[] newb = new AClass[newa.length];
                System.arraycopy(newa, 0, newb, 0, newa.length);
                newb[index] = AClassUtils.getPrimitiveWrapAClass(orgi[index]);
                
                if(primitiveFlag(newb) != orgiFlagValue){
                    list.add(newb);
                }
            }
            if(primitiveFlag(newa) != orgiFlagValue){
                list.add(newa);
            }
        }else{
            if(AClassUtils.canUnboxOrBox(orgi[index])){
                AClass[] newb = new AClass[newa.length];
                System.arraycopy(newa, 0, newb, 0, newa.length);
                newb[index] = AClassUtils.getPrimitiveWrapAClass(orgi[index]);

                allArgumentWithBoxAndUnBox(orgi, orgiFlagValue, index + 1, newb, list);
            }
            allArgumentWithBoxAndUnBox(orgi, orgiFlagValue, index + 1, newa, list);
        }
    }
    

    /**
     * 
     * @param from
     * @param to
     * @return
     */
    public static void autoCastTypeCheck(AClass from, AClass to){
    	if(!from.isChildOrEqual(to)){
            if(AClassUtils.isPrimitiveWrapAClass(from) && 
               AClassUtils.isPrimitiveWrapAClass(to)){
                throw new IllegalArgumentException("Type mismatch: cannot convert from" + from + " to " + to);
            }
            if(!from.isPrimitive() && !to.isPrimitive()){
                throw new IllegalArgumentException("Type mismatch: cannot convert from " + from + " to " + to + " you can add a cast");
            }            
        }
    }
}