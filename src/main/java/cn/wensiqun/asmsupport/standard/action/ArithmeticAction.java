package cn.wensiqun.asmsupport.standard.action;

import cn.wensiqun.asmsupport.core.Parameterized;
import cn.wensiqun.asmsupport.core.operator.numerical.arithmetic.Addition;
import cn.wensiqun.asmsupport.core.operator.numerical.arithmetic.Division;
import cn.wensiqun.asmsupport.core.operator.numerical.arithmetic.Modulus;
import cn.wensiqun.asmsupport.core.operator.numerical.arithmetic.Multiplication;
import cn.wensiqun.asmsupport.core.operator.numerical.arithmetic.Subtraction;


/**
 * 生成算数运算
 *
 * @author wensiqun(at)163.com
 */
public interface ArithmeticAction {
    
    /**
     * 生成加法操作指令例如：factor1 + factor2,对应下面的红色java代码
     * 
     * <p style="border:1px solid;width:300px;padding:10px;">
     * int count = <b style="color:#FF3300">factor1 + factor2;</b>
     * </p>
     * 
     * @param factor1
     * @param factor2
     * @return {@link Addition}
     */
    public Addition add(Parameterized factor1, Parameterized factor2);

    /**
     * 生成减法操作指令例如：factor1 - factor2,对应下面的红色java代码
     * 
     * <p style="border:1px solid;width:300px;padding:10px;">
     * int count = <b style="color:#FF3300">factor1 - factor12;</b>
     * </p>
     * 
     * @param factor1
     * @param factor2
     * @return {@link Subtraction}
     */
    public Subtraction sub(Parameterized factor1, Parameterized factor2);
    
    /**
     * 生成乘法操作指令例如：factor1 * factor2,对应下面的红色java代码
     * 
     * <p style="border:1px solid;width:300px;padding:10px;">
     * int count = <b style="color:#FF3300">factor1 * factor12;</b>
     * </p>
     * 
     * @param factor1
     * @param factor2
     * @return {@link Multiplication}
     */
    public Multiplication mul(Parameterized factor1, Parameterized factor2);
    
    /**
     * generate division instruction : factor1 / factor2
     * 
     * <p style="border:1px solid;width:300px;padding:10px;">
     * int count = <b style="color:#FF3300">factor1 / factor12;</b>
     * </p>
     * 
     * @param factor1
     * @param factor2
     * @return {@link Division}
     */
    public Division div(Parameterized factor1, Parameterized factor2);
    
    /**
     * 
     * generate mod instruction : factor1 % factor2,对应下面的红色java代码
     * 
     * <p style="border:1px solid;width:300px;padding:10px;">
     * int count = <b style="color:#FF3300">factor1 % factor12;</b>
     * </p>
     * 
     * 
     * @param factor1
     * @param factor2
     * @return {@link Modulus}
     */
    public Modulus mod(Parameterized factor1, Parameterized factor2);
    
}