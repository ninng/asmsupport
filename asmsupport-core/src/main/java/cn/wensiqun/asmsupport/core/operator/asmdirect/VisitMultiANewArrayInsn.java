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
package cn.wensiqun.asmsupport.core.operator.asmdirect;

import cn.wensiqun.asmsupport.core.context.MethodExecuteContext;
import cn.wensiqun.asmsupport.core.block.KernelProgramBlock;
import cn.wensiqun.asmsupport.core.operator.AbstractOperator;
import cn.wensiqun.asmsupport.core.operator.Operator;

public class VisitMultiANewArrayInsn extends AbstractOperator {

	private int dims;
	private String desc;

	protected VisitMultiANewArrayInsn(KernelProgramBlock block, String desc, int dims) {
		super(block, Operator.COMMON);
		this.dims = dims;
		this.desc = desc;
	}

	@Override
	protected void doExecute(MethodExecuteContext context) {
		context.getInstructions().getMv().visitMultiANewArrayInsn(desc, dims);
	}

}
