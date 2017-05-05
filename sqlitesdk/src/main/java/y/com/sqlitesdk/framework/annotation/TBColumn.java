package y.com.sqlitesdk.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识这是个行
 * Created by lpds on 2017/4/14.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TBColumn {

    /**
     * 是否允许为空
     * @return
     */
    boolean notNull() default false;

    /**
     * 是否唯一
     * @return
     */
    boolean unique() default false;

    /**
     * 自增长，注释字段需 整形
     * @return
     */
    boolean autoincrememt() default false;

    /**
     * 基本加密存储
     * @return
     */
    @Deprecated
    boolean md5() default false;
}
