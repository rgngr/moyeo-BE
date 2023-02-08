package com.hanghae.finalProject.config.model;

import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class MySQLDialectCustom extends MySQL8Dialect {
     public MySQLDialectCustom() {
          super();
          registerFunction("match",
               new SQLFunctionTemplate(StandardBasicTypes.DOUBLE, "match(?1) against (?2 in boolean mode)"));
     }
}
