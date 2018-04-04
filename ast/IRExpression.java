package ast;

import types.*; 

public class IRExpression extends IRBase {

    public String toJasminString() {
        return "Is it possible you did not implement the getJasminString method on some IRExpression?"; 
    }

    // java7 compatibility cause uvic o7 
    // https://stackoverflow.com/questions/1978933/a-quick-and-easy-way-to-join-array-elements-with-a-separator-the-opposite-of-sp
    public static String strJoin(String[] aArr, String sSep) {
        StringBuilder sbStr = new StringBuilder();
        for (int i = 0, il = aArr.length; i < il; i++) {
            if (i > 0)
                sbStr.append(sSep);
            sbStr.append(aArr[i]);
        }
        return sbStr.toString();
    }
}