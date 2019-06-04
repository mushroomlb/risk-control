package automatically;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.ai.risk.analysis.framework.common.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * <p>
 * mysql 代码生成器演示例子
 * </p>
 *
 * @author Steven
 * @since 2019-04-24
 */
public class MySQLGenerator {

    private static final Logger log = LoggerFactory.getLogger(MySQLGenerator.class);

    /**
     * 以下是代码生成前需要修改的部分:
     */
    private static String author = "Steven";

    //private static String url = "jdbc:mysql://hi-tech.online:3306/base?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
    private static String url = "jdbc:oracle:thin:@10.174.43.37:15219:logdb1";
    private static String driver = "oracle.jdbc.driver.OracleDriver"; //"com.mysql.cj.jdbc.Driver";
    //private static String schemaName = "base";
    private static String username = "ucr_log";
    private static String password = "Ynyd!123";

    private static String parentPackege = "com.ai.risk.analysis.modules";

    /**
     * 以下不用修改
     */

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    private static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        String help = tip + ": ";
        System.out.print(help);
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        //System.out.println("数据源: " + schemaName);
        String moduleName = scanner("请输入本次构建的模块名");
        String[] tables = scanner("请输入本次构建的表名(多个表明用英文逗号隔开)").split(",");

        log.info("本次构建的模块路径为: {}.{}", parentPackege, moduleName);
        log.info("本次构建的表: ");
        for (String table : tables) {
            log.info(" -> " + table);
        }

        String answer = scanner("是否继续? (Y/N)");
        if (!"Y".equalsIgnoreCase(answer)) {
            System.exit(0);
        }

        // 自定义需要填充的字段
        List<TableFill> tableFillList = new ArrayList<>();
        TableFill createField = new TableFill("gmt_create", FieldFill.INSERT);
        TableFill modifiedField = new TableFill("gmt_modified", FieldFill.INSERT_UPDATE);
        tableFillList.add(createField);
        tableFillList.add(modifiedField);

        // 代码生成器
        AutoGenerator autoGenerator = new AutoGenerator();

        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        String projectPath = System.getProperty("user.dir").replace('\\', '/');
        String outPutDir = projectPath + "/src/main/java";
        log.info("样板代码输出路径: " + outPutDir);

        globalConfig.setOutputDir(outPutDir);
        globalConfig.setAuthor(author);
        globalConfig.setOpen(false);
        autoGenerator.setGlobalConfig(globalConfig);

        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.ORACLE);
        dataSourceConfig.setUrl(url);
        //dataSourceConfig.setSchemaName(schemaName);
        dataSourceConfig.setDriverName(driver);
        dataSourceConfig.setUsername(username);
        dataSourceConfig.setPassword(password);
        autoGenerator.setDataSource(dataSourceConfig);

        // 包配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setModuleName(moduleName);
        packageConfig.setParent(parentPackege);
        packageConfig.setEntity("entity.po");
        autoGenerator.setPackageInfo(packageConfig);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
//        focList.add(new FileOutConfig("/templates/controller.java.ftl") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                // 自定义输入文件名称
//                String path = projectPath + "/src/main/java/" + parentPackege.replace('.', '/') + "/" + packageConfig.getModuleName()
//                        + "/" + tableInfo.getEntityName() + "Controller" + StringPool.DOT_JAVA;
//                System.out.println("controller路径为: " + path);
//                return path;
//            }
//        });


        cfg.setFileOutConfigList(focList);
        autoGenerator.setCfg(cfg);
        autoGenerator.setTemplate(new TemplateConfig().setXml(null));

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setTableFillList(tableFillList);
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setInclude(tables);
        strategy.setRestControllerStyle(true);
        //strategy.setSuperEntityClass(BaseEntity.class);
        strategy.setSuperEntityClass("com.ai.risk.analysis.framework.common.BaseEntity");
        strategy.setSuperEntityColumns("id", "gmt_create", "gmt_modified", "enabled");
        strategy.setControllerMappingHyphenStyle(true);
        //strategy.setTablePrefix(packageConfig.getModuleName() + "_");
        strategy.setEntityTableFieldAnnotationEnable(true);

        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setController("/automatically/controller.java");
        templateConfig.setService("/automatically/service.java");
        templateConfig.setServiceImpl("/automatically/serviceImpl.java");
        templateConfig.setEntity("/automatically/entity.java");
        templateConfig.setMapper("/automatically/mapper.java");
        templateConfig.setXml("/automatically/mapper.xml");
        autoGenerator.setTemplate(templateConfig);

        autoGenerator.setStrategy(strategy);
        autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());
        autoGenerator.execute();
    }

}
