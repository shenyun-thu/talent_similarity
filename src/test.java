
import jxl.*;
import jxl.read.biff.BiffException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;



public class test {

    private String filePath;
    private List list = new ArrayList();
    private String pattern = "(\\d)(.*)(\\.)(.*)(0)";//标识得到的分数
    private String pattern1 = "(\\d)(.*)(\\_)(.*)(\\d)";//标识ID
    private String num[][] = new String[1010][10000];
    private int count = 0;//记录相关测试中的项目数

    public test(String filePath){
        this.filePath = filePath;
    }

    private void read_test() throws IOException,BiffException {
        InputStream stream = new FileInputStream(filePath);

        Workbook rwb = Workbook.getWorkbook(stream);
        Sheet sheet = rwb.getSheet(0);
        for(int i = 0;i < sheet.getRows();i++){
            String[] str = new String[sheet.getColumns()];
            Cell cell = null;
            for(int j = 0; j < sheet.getColumns();j++){
                cell = sheet.getCell(j,i);
                str[j] = cell.getContents();
            }
            list.add(str);
        }
    }
    private void outData()
    {
        for(int i = 1;i<list.size();i++){
            String[] str = (String[])list.get(i);
            for(int j = 0;j <str.length;j ++){
                
            }
        }
    }

  

    public static  void main(String[] args) throws  BiffException,IOException{
        test excel = new test("C:\\Users\\shenyun\\Desktop\\query_result.xls");
        excel.read_test();
        excel.outData();
    }
}



 