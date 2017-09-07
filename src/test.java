
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
    public person[] p = new person[1000];
    private String pattern = "(\\d)(.*)(\\.)(.*)(0)";//标识得到的分数

    public test(String filePath) {
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
            int temp = 0;
            String[] str = (String[])list.get(i);
            p[i] = new person();
            p[i].person_id = str[0];
            p[i].test_id = str[7];
            for(int j = 8;j <str.length;j ++){
                boolean isMatch  = Pattern.matches(pattern,str[j]);
                if(isMatch){
                    p[i].scores[temp] = Double.parseDouble(str[j]);
                    temp++;
                }
            }
            p[i].count = temp-1;
        } 
    }
    
    private void print_info(){

        System.out.println(list.size());
        for(int i = 1;i <list.size();i++){
            System.out.println(p[i].person_id + " " + p[i].test_id); 
            for(int j = 0;j <= p[i].count;j++){
                System.out.print(p[i].scores[j]+"\t");
            }
            System.out.println();
        } 
    }

  

    public static  void main(String[] args) throws  BiffException,IOException {
        test excel = new test("C:\\Users\\shenyun\\Desktop\\query_result.xls");
        excel.read_test();
        excel.outData();
        excel.print_info();
    }
}
   
    