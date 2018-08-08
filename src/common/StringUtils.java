package common;

public class StringUtils {

    /**将String[]数组转换为int[]数组**/
    public static int[] FromStringArrToIntArr(String[] arrStr){
        if(arrStr == null)
        {
            return null;
        }
        int[] intArr  = new int[arrStr.length];
        for(int index=0;index<arrStr.length;index++){
            intArr[index] = Integer.parseInt(arrStr[index]);
        }
        return intArr;
    }
}
