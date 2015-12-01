package com.company;

public class Main {

    public static void main(String[] args) throws Exception {
        ClassLoader.getSystemClassLoader().loadClass("com.company.Config");
        ConfigContext config = Config.validationConfig;
//        XMLConfigParser.parseFromSystemFile("src/Conditions.xml");
//        ConfigValidator configValidator = new ConfigValidator(config);
//        config = configValidator.check();
//        BizObj bizObj = new BizObj();
////        Valid valid = new Valid(validations);
////        List<String> invalidMsg = valid.check2(bizObj);
////        System.out.println(Arrays.toString(invalidMsg.toArray()));
//        new Valid(null).test();


        int a = 6;
        switch (a) {
            case 1:
                System.out.println("111");
                break;
            case 2:
                System.out.println("222");
                break;
            case 6:
                System.out.println(666);
                break;
            System.out.println("final");
        }
    }

    public static void foo(Object a) {
        System.out.println(a.getClass().isPrimitive());
    }
}
