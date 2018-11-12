package jp.co.apcom.processor;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

public class TestGenerator extends ClassGenerator<Test> {
	protected TypeSpec generateClass(TypeElement type) {
		MethodSpec main = MethodSpec.methodBuilder("main")
				.addModifiers(Modifier.PUBLIC/*, Modifier.STATIC*/)
				.returns(void.class)
				.addParameter(String[].class, "args")
				.addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
				.build();

		return TypeSpec.classBuilder("Generated_" + type.getSimpleName())
				.addModifiers(Modifier.PUBLIC, Modifier.FINAL)
				.addMethod(main)
				.build();
	}
}
