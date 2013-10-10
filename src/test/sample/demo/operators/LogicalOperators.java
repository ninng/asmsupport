/**
 * 
 */
package demo.operators;


import org.objectweb.asm.Opcodes;

import jw.asmsupport.block.method.common.CommonMethodBody;
import jw.asmsupport.clazz.AClass;
import jw.asmsupport.creator.ClassCreator;
import jw.asmsupport.definition.value.Value;
import jw.asmsupport.definition.variable.LocalVariable;


import demo.CreateMethod;

/**
 * @author 温斯群(Joe Wen)
 * 
 */
public class LogicalOperators extends CreateMethod {

	public LogicalOperators(ClassCreator creator) {
		super(creator);
	}

	@Override
	public void createMethod() {
		creator.createMethod("logicalOperators", null, null, null, null, Opcodes.ACC_PUBLIC,
		        new CommonMethodBody(){
	        @Override
            public void generateBody(LocalVariable... argus) {
	        	LocalVariable b1 = createVariable("b1", AClass.BOOLEAN_ACLASS, false, Value.value(true));
	        	LocalVariable b2 = createVariable("b2", AClass.BOOLEAN_WRAP_ACLASS, false, Value.value(false));
	        	invoke(out, "println", Value.value(" b1 = true "));
	        	invoke(out, "println", Value.value(" b2 = false "));
	        	invoke(out, "println", append(Value.value(" b1 & b2 = "), logicalAnd(b1, b2)));
	        	invoke(out, "println", append(Value.value(" b1 | b2 = "), logicalOr(b1, b2)));
	        	invoke(out, "println", append(Value.value(" b1 ^ b2 = "), logicalXor(b1, b2)));
	        	invoke(out, "println", append(Value.value(" b1 && b2 = "),  conditionalAnd(b1, b2)));
	        	invoke(out, "println", append(Value.value(" b1 || b2 = "), conditionalOr(b1, b2)));
	        	invoke(out, "println", append(Value.value(" !b1 = "), not(b1)));
			    runReturn();
    	    }
        });
	}

}