package y.com.sqlitesdk.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 外键标识(必须在 TBColumn 之下标识)
 * 自动创建触发器，当主键被删除，此外键一并消除
 * Created by lpds on 2017/4/17.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TBForeign {
    /**
     * 关联的字段
     * @return
     */
    String referencesTableField() default "";

    /**
     * 关联的表格
     * @return
     */
    String referencesTableName() default "";
}
