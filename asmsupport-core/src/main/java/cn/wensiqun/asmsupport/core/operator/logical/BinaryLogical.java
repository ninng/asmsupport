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
package cn.wensiqun.asmsupport.core.operator.logical;

import cn.wensiqun.asmsupport.core.block.KernelProgramBlock;
import cn.wensiqun.asmsupport.core.clazz.AClassFactory;
import cn.wensiqun.asmsupport.core.definition.KernelParame;
import cn.wensiqun.asmsupport.core.log.Log;
import cn.wensiqun.asmsupport.core.log.LogFactory;
import cn.wensiqun.asmsupport.core.operator.Operator;
import cn.wensiqun.asmsupport.core.operator.numerical.bit.BinaryBitwise;
import cn.wensiqun.asmsupport.standard.def.clazz.AClass;
import cn.wensiqun.asmsupport.standard.error.ASMSupportException;

/**
 * 
 * @author 温斯群(Joe Wen)
 *
 */
public abstract class BinaryLogical extends AbstractLogical {
    
    private static final Log LOG = LogFactory.getLog(BinaryBitwise.class);
    
    protected KernelParame factor1;
    protected KernelParame factor2;
    
    protected BinaryLogical(KernelProgramBlock block, KernelParame factor1, KernelParame factor2, Operator operator) {
        super(block, operator);
        this.factor1 = factor1;
        this.factor2 = factor2;
    }

	@Override
	protected void verifyArgument() {
		AClass ftrCls1 = factor1.getResultType();
        AClass ftrCls2 = factor2.getResultType();
        
        if(!((ftrCls1.equals(AClassFactory.getType(boolean.class)) || ftrCls1.equals(AClassFactory.getType(Boolean.class))) &&
           (ftrCls2.equals(AClassFactory.getType(boolean.class)) || ftrCls2.equals(AClassFactory.getType(Boolean.class))))){
            throw new ASMSupportException("the factor type must be boolean or Boolean for logical operator!");
        }
	}

	@Override
	protected void checkAsArgument() {
        factor1.asArgument();
        factor2.asArgument();
	}

	@Override
    public void execute() {
        if(byOtherUsed){
            super.execute();
        }else{
            throw new ArithmeticException("the logical operator " + factor1.getResultType() + " " + getOperatorSymbol() + " " + 
                                          factor2.getResultType() + " has not been used by other operator.");
        }
    }

    @Override
    protected void factorToStack() {
        if(LOG.isPrintEnabled()) {
            LOG.print("Factors to stack");
        }
        factor1.loadToStack(block);
        insnHelper.unbox(factor1.getResultType().getType());
        
        factor2.loadToStack(block);
        insnHelper.unbox(factor2.getResultType().getType());
    }

}
