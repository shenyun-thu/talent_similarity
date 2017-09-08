import java.io.*;
import java.util.regex.Pattern;

public class test_csv {
    private String filePath;
    public static int N = 5000000;
    public person[] p = new person[N];
    public person[] p_temp = new person[N];
    public static String[]  test_id = new String[10000];
    public int count_person = 0;
    public int count_temp = 0;
    public int num_testID = 0;
    public int count_ans_all = 0;
    public double similarity_per;
    public static double similarity_limit = 0.9; //相似度的限制
    public static double low_score_limit = 80.0; //各维度平均分最低值限制
    public static double max_minus_limit = 2.0;//各维度上的差值的最大限制

    test_csv(String filePath){
        this.filePath = filePath;
    }
    
    public void read_csv(){
        File csv = new File(filePath);
        String pattern = "(\\d*)(\\.)(\\d*)";
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader(csv));
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        String line = "";
        try{
            line = br.readLine();
            while((line = br.readLine()) != null){
                int count_s = 0;
                String[] temp = line.split("\\,");
                p[count_person] = new person();
                p[count_person].person_id = temp[0];
                p[count_person].test_id = temp[7];
                for(int i = 8; i <temp.length;i++){
                    boolean isMatch = Pattern.matches(pattern,temp[i]);
                    if(isMatch){
                        p[count_person].scores[count_s] = Double.parseDouble(temp[i]);
                        count_s ++;
                    }
                }
                p[count_person].count = count_s;
                
                for(int j = 0;j < p[count_person].count;j++){
                    p[count_person].average += p[count_person].scores[j];
                }
                p[count_person].average /= (p[count_person].count);

                count_person++;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    
    public void classify_test(){
        num_testID = 0;
        test_id[num_testID] = new String();
        test_id[num_testID] = p[0].test_id;
        num_testID++;
        for(int i=1;i<count_person;i++){
            boolean flag = true;
            test_id[num_testID] = new String();
            for(int j = 0;j <= num_testID;j++){
                if(test_id[j].equals(p[i].test_id)){
                    flag = false;
                    break;
                }
            }
            if(flag){
                test_id[num_testID] = p[i].test_id;
                num_testID++;
            }
        }
    }
    
    public void init_testID(String test_id){
        count_temp = 0;
        for(int i=0;i<count_person;i++){
            if(p[i].test_id.equals(test_id)&&p[i].average > low_score_limit){
                p_temp[count_temp] = p[i];
                count_temp ++;
            }
        }
    }
    
    public void cal_eu_distance(){
        boolean have_ans = false;
        int count_ans = 0;
        double max_score_minus = 0;
        for(int i=0;i<count_temp;i++){
            for(int j =i+1;j<count_temp;j++){
                double ans = 0;
                boolean can_cal = true;
                for(int k = 0;k <= p_temp[i].count;k++){
                    if((p_temp[i].scores[k] - p_temp[j].scores[k]) > max_score_minus){
                        max_score_minus = (p_temp[i].scores[k] - p_temp[j].scores[k]);
                        if(max_score_minus > max_minus_limit) {
                            can_cal = false;
                            break;
                        }
                    }
                }
                if(can_cal){
                    for (int k = 0; k <p_temp[i].count; k++) {
                        ans += (p_temp[i].scores[k] - p_temp[j].scores[k])*(p_temp[i].scores[k] - p_temp[j].scores[k]);
                    }
                    ans = Math.sqrt(ans);
                    similarity_per = 1 / (1 + 0.1 * ans);
                    if(similarity_per > similarity_limit){
                        have_ans = true;
                        System.out.println("the similarity between id " + p_temp[i].person_id + " and id " + p_temp[j].person_id + " is " + similarity_per*100 + "%");
                        System.out.println("the actual statistics are : ");
                        for(int m = 0;m < p_temp[i].count;m++){
                            System.out.print(p_temp[i].scores[m] + " ");
                        }
                        System.out.println("the average : " + p_temp[i].average);
                        System.out.println();
                        for(int m = 0;m < p_temp[j].count;m++){
                            System.out.print(p_temp[j].scores[m] + " ");
                        }
                        System.out.println("the average : " + p_temp[j].average);
                        System.out.println();
                        count_ans ++;
                    }
                }
            }
        }
        count_ans_all += count_ans;
        System.out.println("in this test we find " + count_ans + " groups");
        if(!have_ans) System.out.println("No answer for your request");
    }
    
    public void show(){ 
        for(int i=0;i<num_testID;i++){
            init_testID(test_id[i]);
            System.out.println("test_id : "+test_id[i]+" the result of talent_similarity judgment: ");
            System.out.println("-----------------------");
            cal_eu_distance();
            System.out.println();
        }
        System.out.println("in this tenant we all find "+count_ans_all+" groups");
        System.out.println("in this tenant we classify "+num_testID+" groups");
        System.out.println("we have " + count_person +" people");
    }
    
    public void print(){
        for(int i = 0; i < count_person;i++){
            System.out.print(p[i].person_id + " " + p[i].average+" ");
            for(int j = 0;j<p[i].count;j++){
                System.out.print(p[i].scores[j] + " ");
            }
            System.out.println();
        }
    }
    public static void main(String[] args) {
        long start_time = System.currentTimeMillis();
        test_csv demo = new test_csv("C:\\Users\\shenyun\\Desktop\\store\\tenantID_101120.csv");
        demo.read_csv();
     //   demo.print();
        demo.classify_test();
        demo.show();
        long end_time = System.currentTimeMillis();
        System.out.println("程序运行时间为 : "+(end_time - start_time)+" ms");
    }
}
