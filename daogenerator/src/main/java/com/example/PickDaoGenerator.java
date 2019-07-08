package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class PickDaoGenerator {

    public static void main(String[] args) throws Exception{
        Schema schema = new Schema(1, "com.frxs.check.greendao");
//      Schema schema = new Schema(1, "me.itangqi.bean");
//      schema.setDefaultJavaPackageDao("me.itangqi.dao");

        // schema2.enableActiveEntitiesByDefault();
        // schema2.enableKeepSectionsByDefault();
//        addNote(schema);
        addNotes(schema);

        new DaoGenerator().generateAll(schema, "../NewCheckApp/app/src/main/java");
    }

    /*
     * @param schema
     */
    private static void addNotes(Schema schema){
        // 个实体（类）就关联到数据库中的一张表，此处表名为「Goods」（既类名）

    }

}
