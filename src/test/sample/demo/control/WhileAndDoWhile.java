package demo.control;


import org.objectweb.asm.Opcodes;

import jw.asmsupport.block.control.DoWhileLoop;
import jw.asmsupport.block.control.IF;
import jw.asmsupport.block.control.WhileLoop;
import jw.asmsupport.block.method.common.CommonMethodBody;
import jw.asmsupport.clazz.AClass;
import jw.asmsupport.creator.ClassCreator;
import jw.asmsupport.definition.value.Value;
import jw.asmsupport.definition.variable.LocalVariable;


import demo.CreateMethod;

public class WhileAndDoWhile extends CreateMethod  {

	public WhileAndDoWhile(ClassCreator creator) {
		super(creator);
	}

	@Override
	public void createMethod() {

		creator.createMethod("whileAndDoWhile", null, null, null, null, Opcodes.ACC_PUBLIC,
		        new CommonMethodBody(){
					@Override
					public void generateBody(LocalVariable... argus) {
						final LocalVariable i = createVariable("i", AClass.INT_ACLASS, false, Value.value(0));
			        	
			        	whileloop(new WhileLoop(lessThan(i, Value.value(5))){
							
			        		@Override
							public void generateInsn() {
			        			ifthan(new IF(equal(i, Value.value(3))){
									@Override
									public void generateInsn() {
										breakout();
									}
			        			});
			        			invoke(out, "println", append(Value.value(" i = "), afterInc(i)));
						    }
			        	});
			        	
			        	assign(i, Value.value(0));
			        	
                        dowhile(new DoWhileLoop(lessThan(i, Value.value(5))){
							
			        		@Override
							public void generateInsn() {
			        			invoke(out, "println", append(Value.value(" i = "), afterInc(i)));
			        			ifthan(new IF(equal(i, Value.value(3))){
									@Override
									public void generateInsn() {
										afterInc(i);
										continueout();
									}
			        			});
						}});
                        
					    runReturn();
					}
		        }
		);
	}

}