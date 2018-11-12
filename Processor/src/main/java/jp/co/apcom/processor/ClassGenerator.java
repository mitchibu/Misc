package jp.co.apcom.processor;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

public abstract class ClassGenerator<T extends Annotation> {
	@SuppressWarnings("unchecked")
	public boolean generate(RoundEnvironment roundEnvironment, ProcessingEnvironment processingEnvironment) {
		Class<? extends Annotation> clazz = (Class<? extends Annotation>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		Collection<? extends Element> annotatedElements = roundEnvironment.getElementsAnnotatedWith(clazz);
		List<TypeElement> types = ElementFilter.typesIn(annotatedElements);

		Filer out = processingEnvironment.getFiler();
		for(TypeElement type : types) {
			JavaFile javaFile = JavaFile.builder(
					processingEnvironment.getElementUtils().getPackageOf(type).getQualifiedName().toString(),
					generateClass(type)).build();
			try {
				javaFile.writeTo(out);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	protected abstract TypeSpec generateClass(TypeElement type);
}
