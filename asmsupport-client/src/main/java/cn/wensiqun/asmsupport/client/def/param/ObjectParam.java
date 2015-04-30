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
package cn.wensiqun.asmsupport.client.def.param;

import cn.wensiqun.asmsupport.client.block.KernelProgramBlockCursor;
import cn.wensiqun.asmsupport.client.def.Param;
import cn.wensiqun.asmsupport.client.def.ParamPostern;
import cn.wensiqun.asmsupport.client.def.action.EqualAction;
import cn.wensiqun.asmsupport.client.def.action.InstanceofAction;
import cn.wensiqun.asmsupport.client.def.action.NotEqualAction;
import cn.wensiqun.asmsupport.client.def.behavior.ObjectBehavior;
import cn.wensiqun.asmsupport.core.definition.KernelParame;
import cn.wensiqun.asmsupport.core.definition.value.Value;
import cn.wensiqun.asmsupport.standard.def.clazz.AClass;

public class ObjectParam extends DummyParam implements ObjectBehavior {

    public ObjectParam(KernelProgramBlockCursor cursor, KernelParame target) {
        super(cursor, target);
    }

    @Override
    public UncertainParam call(String methodName, Param... arguments) {
    	if(target instanceof Value && ((Value)target).getValue() instanceof AClass) {
    		return new UncertainParam(cursor, 
    				cursor.getPointer().call(
    						(AClass)((Value)target).getValue(), 
    						methodName, ParamPostern.getTarget(arguments)));
    	}
        return new UncertainParam(cursor, cursor.getPointer().call(target, methodName, ParamPostern.getTarget(arguments)));
    }

    @Override
    public ObjectParam stradd(Param param) {
        return new ObjectParam(cursor, cursor.getPointer().stradd(target, ParamPostern.getTarget(param)));
    }

    @Override
    public UncertainParam cast(Class<?> type) {
        return new UncertainParam(cursor, cursor.getPointer().checkcast(target, type));
    }

    @Override
    public UncertainParam cast(AClass type) {
        return new UncertainParam(cursor, cursor.getPointer().checkcast(target, type));
    }

    @Override
    public BoolParam instanceof_(Class<?> type) {
        return new BoolParam(cursor, new InstanceofAction(cursor, cursor.getPointer().getType(type)), this);
    }

    @Override
    public BoolParam instanceof_(AClass type) {
        return new BoolParam(cursor, new InstanceofAction(cursor, type), this);
    }

    @Override
    public BoolParam eq(Param para) {
        return new BoolParam(cursor, new EqualAction(cursor), this);
    }

    @Override
    public BoolParam ne(Param para) {
        return new BoolParam(cursor, new NotEqualAction(cursor), this);
    }

}
