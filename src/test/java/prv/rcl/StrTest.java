package prv.rcl;


import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class StrTest {

    @Test
    void test() {
        String str = "F_ICHAR01,editStyle=1;\n" +
                "F_ICHAR02,editStyle=1;\n" +
                "F_ICHAR05,editStyle=6;COL_EDITVIEW=0:可提前支取@1:不可提前支取;DESCOL=F_TQZQ;\n" +
                "F_ICHAR06,editStyle=6;COL_EDITVIEW=0:可转让@1:不可转让;DESCOL=F_SFZR;\n" +
                "F_IRQ01,editStyle=5;\n" +
                "F_IRQ02,editStyle=5;\n" +
                "F_ICHAR07,editStyle=7;viewDataSetID=ZJ_ZHXX;\n" +
                "F_ICHAR08,editStyle=7;viewDataSetID=ZJ_KHJG;VECID=F_JGMC;\n" +
                "F_ICHAR09,editStyle=7;viewDataSetID=ZJ_ZHXX;\n" +
                "F_ICHAR10,editStyle=7;viewDataSetID=ZJ_KHJG;VECID=F_JGMC;\n" +
                "F_INUM04,editStyle=3;DECN=2;\n" +
                "F_INUM01,editStyle=3;DECN=2;\n" +
                "F_INUM02,editStyle=3;DECN=2;\n" +
                "F_ICHAR11,editStyle=3;DECN=2;\n" +
                "F_INUM06,DESCOL=F_BCJE\n";
        String valueStr = "产品编号,F_ICHAR01\n" +
                "产品名称,F_ICHAR02\n" +
                "是否可提前支取,F_ICHAR05\n" +
                "是否可提前转让,F_ICHAR06\n" +
                "支取日期,F_IRQ01\n" +
                "转让日期,F_IRQ02\n" +
                "付款银行账号,F_ICHAR07\n" +
                "付款银行开户行,F_ICHAR08\n" +
                "收款银行账号,F_ICHAR09\n" +
                "收款银行开户行,F_ICHAR10\n" +
                "购买金额,F_INUM04\n" +
                "累计支取金额,F_INUM01\n" +
                "累计转让金额,F_INUM02\n" +
                "期间收益,F_ICHAR11\n" +
                " ,F_INUM06\n";
        String[] split = str.split("\n");
//        s.substring(s.indexOf("DESCOL="))
        Map<String, String> stringMap = Arrays.stream(split)
                .map(s -> s.split(","))
                .collect(Collectors.toMap(k -> k[0].trim(), v -> v[1].trim()));
        String[] split1 = valueStr.split("\n");
        Map<String, String> map = Arrays.stream(split1)
                .map(s -> s.split(","))
                .collect(Collectors.toMap(k -> k[1], v -> v[0]));
        stringMap.forEach((k, v) -> {
            String s1 = map.get(k);
//            System.out.println(k);
//            System.out.println(s1);
            if (v.contains("DESCOL=")) {
                String s = v.substring(v.indexOf("DESCOL=") + 7);
                if (s.contains(";")) {
                    s = s.substring(0, s.indexOf(";"));
                }
                if (StringUtils.hasText(s1)) {
//                    System.out.print(k + "," + s1 + "," + s + "\n");
                    System.out.println(s);
                }
            } else {
                System.out.println();
//                System.out.println(s1);
//                System.out.print(k + "," + s1 + "\n");
            }
        });
    }

}
