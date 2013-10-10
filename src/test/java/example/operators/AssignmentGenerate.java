package example.operators;


import org.objectweb.asm.Opcodes;

import jw.asmsupport.block.method.common.StaticMethodBody;
import jw.asmsupport.clazz.AClass;
import jw.asmsupport.clazz.AClassFactory;
import jw.asmsupport.creator.ClassCreator;
import jw.asmsupport.definition.value.Value;
import jw.asmsupport.definition.variable.LocalVariable;


import example.AbstractExample;

public class AssignmentGenerate extends AbstractExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClassCreator creator = new ClassCreator(Opcodes.V1_5, Opcodes.ACC_PUBLIC , "generated.operators.AssignmentGenerateExample", null, null);

		creator.createStaticMethod("commonMethod", null, null, AClass.STRING_ACLASS, null, Opcodes.ACC_PUBLIC, new StaticMethodBody(){

			@Override
			public void generateBody(LocalVariable... argus) {
				runReturn(Value.value("I'm from commonMethod"));
			}
		});
		
		creator.createStaticMethod("main", new AClass[]{AClassFactory.getProductClass(String[].class)}, new String[]{"args"}, null, null,
				Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, new StaticMethodBody(){

			@Override
			public void generateBody(LocalVariable... argus) {
				//创建个String变量默认赋值为null
				LocalVariable string = createVariable("string", AClass.STRING_ACLASS, false, null);
				
				assign(string, invokeStatic(getMethodOwner(), "commonMethod"));
				invoke(systemOut, "println", append(Value.value("first asign :"), string));
				
				assign(string, Value.value("second assing value"));
				invoke(systemOut, "println", append(Value.value("second asign :"), string));
				
				runReturn();
			}
        });
		generate(creator);
	}
}