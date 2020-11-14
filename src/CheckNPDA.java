import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class CheckNPDA {
    static int count = 0;//str的位置指针

    public boolean checkNPDA(String filename, String str) throws IOException {
        if (str == null)return false;
        //预处理 获取格里巴赫范式list 创建下推自动机的栈 str转为char数组
        List<ContextFreeNode> list = new LinkedList<>();//存格里巴赫文法
        ChangeToGreibach changeToGreibach = new ChangeToGreibach();
        list = changeToGreibach.readRule(list,filename);
        System.out.println("格里巴赫范式：");
        changeToGreibach.showRules(list);
        System.out.println("给定字符串：" + str);
        Stack<Character> stk = new Stack<>();//下推自动机的栈
        Stack<Character> stk_copy;
        char[] input = str.toCharArray();

        //judge
        if (input.length == 0){
            for (ContextFreeNode node:list){
                if (node.getFirst().equals("S") && node.getSecond().equals("$"))return true;
            }
            return false;
        }
        for (int i = 0; i < list.size(); i++){
            count = 0;
            stk.clear();
            //System.out.println(stk);
            ContextFreeNode node = list.get(i);
            char left = node.getFirst().charAt(0);
            char[] right = node.getSecond().toCharArray();
            if (left == 'S' && right[0] == input[count]){
                count++;
                for (int j = right.length - 1;j > 0; j--)stk.add(right[j]);
                stk_copy = (Stack<Character>) stk.clone();
                //递归判断是否能推出
                if (judge(list,stk_copy,input))return true;
                else count--;
            }
        }
        //System.out.println("未找到~");
        return false;
    }

    public boolean judge(List<ContextFreeNode> list, Stack<Character> stk,char[] input){
        if (stk.isEmpty()){
            if (count == input.length)return true;
            else return false;
        }else {
            if (count == input.length)return false;
        }

        Stack<Character> stk_copy;
        for (int i = 0; i < list.size();i++){
            //还原栈
            stk_copy = (Stack<Character>) stk.clone();
            ContextFreeNode node = list.get(i);
            char left = node.getFirst().charAt(0);
            char[] right = node.getSecond().toCharArray();
            if (left == stk_copy.peek() && right[0] == input[count]){
                stk_copy.pop();
                count++;
                for (int j = right.length - 1;j > 0; j--)stk_copy.add(right[j]);
                if (judge(list,stk_copy,input))return true;
                else count--;
            }
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        CheckNPDA chkNPDA = new CheckNPDA();
        if(chkNPDA.checkNPDA("./Data/greibach_output.txt","aabcabcabcabcabc")){
            System.out.println("接受~");
        }else {
            System.out.println("失败!");
        }
    }
}
