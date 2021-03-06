
package cn.wensiqun.asmsupport.client.def.action;

import cn.wensiqun.asmsupport.client.def.Param;
import cn.wensiqun.asmsupport.client.def.param.basic.DummyParam;
import cn.wensiqun.asmsupport.core.operator.Operator;
import cn.wensiqun.asmsupport.core.utils.common.BlockTracker;

public class NegAction extends AbstractUnaryAction {

    public NegAction(BlockTracker tracker) {
        super(tracker, Operator.NEG);
    }

    @Override
    public Param doAction(Param... operands) {
       return new DummyParam(tracker, tracker.track().neg(operands[0].getTarget()));
    }

}
