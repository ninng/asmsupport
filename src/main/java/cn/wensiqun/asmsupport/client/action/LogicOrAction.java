package cn.wensiqun.asmsupport.client.action;

import cn.wensiqun.asmsupport.client.Param;
import cn.wensiqun.asmsupport.client.ParamPostern;
import cn.wensiqun.asmsupport.client.def.DummyParam;
import cn.wensiqun.asmsupport.core.block.ProgramBlockInternal;
import cn.wensiqun.asmsupport.core.operator.Operator;
import cn.wensiqun.asmsupport.org.apache.commons.collections.ArrayStack;

public class LogicOrAction extends OperatorAction {

    public LogicOrAction(ProgramBlockInternal block) {
        super(block, Operator.BIT_OR);
    }

    @Override
    public Param doAction(ArrayStack<Param> operands) {
        Param fac2 = operands.pop();
        Param fac1 = operands.pop();
        return new DummyParam(block.logicalOr(ParamPostern.getTarget(fac1), ParamPostern.getTarget(fac2)));
    }

}
