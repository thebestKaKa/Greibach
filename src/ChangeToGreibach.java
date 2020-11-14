import java.io.*;
import java.util.*;

public class ChangeToGreibach {
    //判断已经使用的非终结符 0表示未用
    private int[] alphabet = new int[26];
    //读取产生式左右两端
    public List<ContextFreeNode> readRule(List<ContextFreeNode> list,String filePath) throws IOException {
        list.clear();
        try {
            String encoding = "utf-8";
            File file = new File(filePath);
            if (file.isFile() && file.exists()){//判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    //读文件的行readline,每次使用都读一行
                    String arr[]=lineTxt.split(" ");
                    ContextFreeNode node = new ContextFreeNode();
                    node.setFirst(arr[0]);
                    node.setSecond(arr[2]);
                    list.add(node);
                }
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        }catch (Exception e){
            System.out.println("读取文件错误！");
            e.printStackTrace();
        }
        return list;
    }
    //判断是否是上下文无关文法
    public void judgeContextFreeGrammar(List<ContextFreeNode> list){
        boolean flag = true;
        try {
            for (int i = 0;i < list.size();i++){
                ContextFreeNode temp = list.get(i);
                //System.out.println(temp.getFirst() + ' ' + temp.getSecond());
                char[] left_arr = temp.getFirst().toCharArray();
                char[] right_arr = temp.getSecond().toCharArray();
                if (left_arr.length > 1 || left_arr.length > right_arr.length){
                    flag = false;
                    break;
                }else if (!Character.isUpperCase(left_arr[0])){
                    flag = false;
                    break;
                }else {
                    //设置已用
                    alphabet[left_arr[0] - 'A'] = 1;
                }
            }
            if (flag == true)System.out.println("给定文法属于上下文无关文法。");
            else System.out.println("给定文法不属于上下文无关文法！！！");
        }catch (Exception e){
            System.out.println("给定文法不属于上下文无关文法！！！");
            e.printStackTrace();
        }
    }
    //消除无用符号(不能推出终结符的符号)
    public void removeUselessSign(List<ContextFreeNode> list){
        System.out.println("消除无用符号！");
        HashSet<Character> isUseful = new HashSet<>();
        HashSet<Character> isUseful_pre = (HashSet<Character>) isUseful.clone();
        isUseful_pre.add('a');
        //get useful
        while (isUseful.size() != isUseful_pre.size()){
            isUseful_pre = (HashSet<Character>) isUseful.clone();
            for (int count0 = 0;count0 < list.size();count0++){
                ContextFreeNode node = list.get(count0);
                char left = node.getFirst().charAt(0);
                char[] right = node.getSecond().toCharArray();
                if (right.length == 1 && (Character.isLowerCase(right[0]) || right[0] == '$')){
                    isUseful.add(left);
                    continue;
                }
                boolean flag = true;
                for (int count1 = 0; count1 < right.length;count1++){
                    if (Character.isUpperCase(right[count1])){
                        flag = flag && isUseful.contains(right[count1]);
                    }
                }
                if (flag)isUseful.add(left);
            }
        }//end while
        //System.out.println(isUseful);
        for (int count2 = 0; count2 < list.size();count2++){
            ContextFreeNode node = list.get(count2);
            char left = node.getFirst().charAt(0);
            char[] right = node.getSecond().toCharArray();
            if (!isUseful.contains(left)){
                list.remove(count2);
                count2--;
                continue;
            }
            for (int count3 = 0; count3 < right.length;count3++){
                if (Character.isUpperCase(right[count3]) && !isUseful.contains(right[count3])){
                    list.remove(count2);
                    count2--;
                    break;
                }
            }
        }
        //S推不出来
        isUseful.clear();
        isUseful_pre.clear();
        isUseful.add('S');
        while (isUseful.size() != isUseful_pre.size()){
            isUseful_pre = (HashSet<Character>) isUseful.clone();
            for (int count4 = 0; count4 < list.size(); count4++){
                ContextFreeNode node = list.get(count4);
                char left = node.getFirst().charAt(0);
                char[] right = node.getSecond().toCharArray();
                if (isUseful.contains(left)){
                    for(int count5 = 0; count5 < right.length;count5++){
                        if (Character.isUpperCase(right[count5]))
                            isUseful.add(right[count5]);
                    }
                }
            }
        }
        for (int count6 = 0; count6 < list.size();count6++){
            ContextFreeNode node = list.get(count6);
            if(!isUseful.contains(node.getFirst().charAt(0))){
                list.remove(count6);
                count6--;
                continue;
            }
        }
    }
    //消除空产生式 考虑一个产生式中多个非终都能推出空 考虑一个非终只有推空一个产生式
    //S -> e不是空产生式
    public void removeE(List<ContextFreeNode> list){
        System.out.println("消除空产生式！");
        Set<ContextFreeNode> set = new HashSet<>();
        String[] production_right;
        for (int i = 0; i < list.size();i++){
            ContextFreeNode node_temp = list.get(i);
            char[] arr_right = node_temp.getSecond().toCharArray();
            char[] arr_left = node_temp.getFirst().toCharArray();
            //如果S->e不处理
            if (arr_right[0] == '$' && arr_left[0] != 'S'){
                list.remove(i);
                i--;
                char V = arr_left[0];
                production_right = new String[50];
                int count = 0;
                // 获取推空非终右端字符串
                for (ContextFreeNode node:list){
                    if (node.getFirst().charAt(0) == V && (!node.getSecond().equals("$")))
                        production_right[count++] = node.getSecond();
                }
                replaceV(list,V,production_right,set);
                for(ContextFreeNode node:set){
                    list.add(node);
                }
                set.clear();
            }
        }
    }
    //替换V V为推出空的非终结符
    public void replaceV(List<ContextFreeNode> list, char V, String[] production_right, Set<ContextFreeNode> set){
        Iterator<ContextFreeNode> iterator = list.iterator();
        while (iterator.hasNext()){
            ContextFreeNode node = iterator.next();
            char [] arr_right = node.getSecond().toCharArray();
            for(int i = 0;i < arr_right.length;i++) {
                if (arr_right[i] == V) {
                    //只有推空一个
                    if (production_right[0] == null){
                        StringBuilder stringBuilder = new StringBuilder(node.getSecond());
                        stringBuilder.deleteCharAt(i);
                        node.setSecond(stringBuilder.toString());
                        if (arr_right.length == 1){
                            //此时还有A->B是因为还未消除单一产生式
                            iterator.remove();
                            ContextFreeNode node_add = new ContextFreeNode();
                            node_add.setFirst(node.getFirst());
                            node_add.setSecond("$");
                            set.add(node_add);
                        }
                    }else {//除了推空还有别的产生式
                        ContextFreeNode node_add = new ContextFreeNode();
                        node_add.setFirst(node.getFirst());
                        StringBuilder stringBuilder = new StringBuilder(node.getSecond());
                        //如果是单一产生式会推出类如 A ->
                        stringBuilder.deleteCharAt(i);
                        if (arr_right.length == 1){
                            //此时还有A->B是因为还未消除单一产生式
                            node_add.setSecond("$");
                        }else {
                            node_add.setSecond(stringBuilder.toString());
                        }
                        set.add(node_add);
                    }
                }
            }
        }
    }
    //消除单一产生式
    public void removeUnitProduction(List<ContextFreeNode> list){
        System.out.println("消除单一产生式!");
        for (int i = 0; i < list.size();i++){
            ContextFreeNode node = list.get(i);
            char[] arr = node.getSecond().toCharArray();
            //判断出单一产生式
            if (arr.length == 1 && Character.isUpperCase(arr[0])){
                char V = arr[0];
                String[] production_right = new String[50];
                list.remove(i);
                i--;
                int count = 0;
                // 获取单一产生式右端字符串
                for (ContextFreeNode node_temp:list){
                    if (node_temp.getFirst().charAt(0) == V)
                        production_right[count++] = node_temp.getSecond();
                }
                for (int j = 0; j < count; j++){
                    ContextFreeNode node_new = new ContextFreeNode();
                    node_new.setFirst(node.getFirst());
                    node_new.setSecond(production_right[j]);
                    list.add(node_new);
                }
            }
        }
    }
    //消除直接左递归！！！
    public void removeLeftRecursive(List<ContextFreeNode> list){
        //System.out.println("消除左递归");
        for (int i = 0; i < list.size();i++){
            ContextFreeNode node = list.get(i);
            char[] arr_right = node.getSecond().toCharArray();
            if (arr_right[0] == node.getFirst().charAt(0)){
                //新的状态
                char node_left = '$';
                for (int m = 0; m < alphabet.length;m++){
                    if (alphabet[m] == 0) {
                        alphabet[m] = 1;
                        node_left = (char)('A' + m);
                        break;
                    }
                }
                list.remove(i);
                i--;
                ContextFreeNode node_new1 = new ContextFreeNode();
                ContextFreeNode node_new2 = new ContextFreeNode();
                node_new1.setFirst(String.valueOf(node_left));
                StringBuilder stringBuilder = new StringBuilder(node.getSecond());
                stringBuilder.deleteCharAt(0);
                stringBuilder.append(String.valueOf(node_left));
                node_new1.setSecond(stringBuilder.toString());
                node_new2.setFirst(String.valueOf(node_left));
//                StringBuilder s = new StringBuilder(node.getSecond());
//                s.deleteCharAt(0);
                node_new2.setSecond("$");
                list.add(node_new1);
                list.add(node_new2);
                for (ContextFreeNode node_temp:list){
                    if (node_temp.getFirst().equals(node.getFirst())){
                        if (node_temp.getSecond().equals("$")){
                            node_temp.setSecond(String.valueOf(node_left));
                            continue;
                        }
                        StringBuilder str = new StringBuilder(node_temp.getSecond());
                        str.append(node_left);
                        node_temp.setSecond(str.toString());
                    }
                }
            }
        }
        removeE(list);
    }
    //把右边第一个为非终结符的替换掉
    public void greibachFirstStep(List<ContextFreeNode> list){
        System.out.println("把产生式右部变为终结符! 消除间接左递归！");
        for(int i = 0; i < list.size();i++){
            ContextFreeNode node = list.get(i);
            char[] arr_right = node.getSecond().toCharArray();
            String left = node.getFirst();
            //把右边第一个非终结符变成终结符
            if (Character.isUpperCase(arr_right[0])){
                //消除间接左递归
                if (arr_right[0] == node.getFirst().charAt(0)){
                    //新的状态
                    char node_left = '$';
                    for (int m = 0; m < alphabet.length;m++){
                        if (alphabet[m] == 0) {
                            alphabet[m] = 1;
                            node_left = (char)('A' + m);
                            break;
                        }
                    }
                    list.remove(i);
                    i--;
                    ContextFreeNode node_new1 = new ContextFreeNode();
                    ContextFreeNode node_new2 = new ContextFreeNode();
                    node_new1.setFirst(String.valueOf(node_left));
                    StringBuilder stringBuilder = new StringBuilder(node.getSecond());
                    stringBuilder.deleteCharAt(0);
                    stringBuilder.append(String.valueOf(node_left));
                    node_new1.setSecond(stringBuilder.toString());
                    node_new2.setFirst(String.valueOf(node_left));
//                    StringBuilder s = new StringBuilder(node.getSecond());
//                    s.deleteCharAt(0);
                    node_new2.setSecond("$");
                    list.add(node_new1);
                    list.add(node_new2);
                    for (ContextFreeNode node_temp:list){
                        if (node_temp.getFirst().equals(node.getFirst())){
                            if (node_temp.getSecond().equals("$")){
                                node_temp.setSecond(String.valueOf(node_left));
                                continue;
                            }
                            StringBuilder str = new StringBuilder(node_temp.getSecond());
                            str.append(node_left);
                            node_temp.setSecond(str.toString());
                        }
                    }
                    continue;
                }
                //右边第一个为非终结符
                String[] toBeReplaced = new String[50];
                list.remove(i);
                i--;
                int count = 0;
                for (ContextFreeNode node_temp:list){
                    if (node_temp.getFirst().charAt(0) == arr_right[0])
                        toBeReplaced[count++] = node_temp.getSecond();
                }
                for (int j = 0; j < count;j++){
                    ContextFreeNode node_new = new ContextFreeNode();
                    node_new.setFirst(left);
                    //使用stringbuilder修改字符串
                    StringBuilder stringBuilder = new StringBuilder(node.getSecond());
                    stringBuilder.deleteCharAt(0);
                    stringBuilder.insert(0,toBeReplaced[j]);
                    node_new.setSecond(stringBuilder.toString());
                    list.add(node_new);
                }
            }
            showRule(list);
            System.out.println("--------------------------");
        }
        removeE(list);
    }
    //将右边转换为格里巴克范式
    public void greibachSecondStep(List<ContextFreeNode> list){
        System.out.println("格里巴赫范式如下：");
        for (int i = 0; i < list.size();i++){
            ContextFreeNode node = list.get(i);
            char[] arr_right = node.getSecond().toCharArray();
            //从1开始 不判断第一个
            for (int j = 1; j < arr_right.length;j++){
                if (Character.isLowerCase(arr_right[j])){
                    StringBuilder str = new StringBuilder(node.getSecond());
                    str.deleteCharAt(j);
                    String new_j = lookForV(list, arr_right[j]);
                    //如果已有 则直接替换 否则 去字母表查询未用的来替换并新增节点
                    if (new_j != null){
                        str.insert(j,new_j);
                    }else {
                        ContextFreeNode node_new = new ContextFreeNode();
                        char left = '$';
                        for (int m = 0; m < alphabet.length;m++){
                            if (alphabet[m] == 0) {
                                alphabet[m] = 1;
                                left = (char)('A' + m);
                                break;
                            }
                        }
                        str.insert(j,left);
                        node_new.setFirst(String.valueOf(left));
                        node_new.setSecond(String.valueOf(arr_right[j]));
                        list.add(node_new);
                    }
                    node.setSecond(str.toString());
                }
            }
        }
    }
    //需要加一个判断条件 只有推出所需终结符的唯一产生式
    public String lookForV(List<ContextFreeNode> list, char t){
        for (ContextFreeNode node : list){
            char[] arr_right = node.getSecond().toCharArray();
            int count = 0;
            for (int i = 0; i < list.size();i++){
                if (list.get(i).getFirst().equals(node.getFirst()))count++;
            }
            if (arr_right.length == 1 && arr_right[0] == t && count == 1){
                return node.getFirst();
            }
        }
        return null;
    }

    public void showRules(List<ContextFreeNode> list){
        sort(list);
        eliminateDuplication(list);
        for (ContextFreeNode node:list){
            System.out.println(node.getFirst() + " -> " + node.getSecond());
        }
    }
    public void showRule(List<ContextFreeNode> list){
        eliminateDuplication(list);
        for (ContextFreeNode node:list){
            System.out.println(node.getFirst() + " -> " + node.getSecond());
        }
    }
    public void sort(List<ContextFreeNode> list){
        //根据list对象某个属性排序
        list.sort(Comparator.comparing(ContextFreeNode::getFirst));
        List<ContextFreeNode> list_others = new LinkedList();
        for(int i = 0;i < list.size();i++){
            if (!list.get(i).getFirst().equals("S")){
                list_others.add(list.get(i));
                list.remove(i);
                i--;
            }
        }
        list.addAll(list_others);
    }

    public void eliminateDuplication(List<ContextFreeNode> list){
        for (int i = 0; i < list.size();i++){
            for (int j = i + 1;j < list.size();j++){
                if (list.get(i).getFirst().equals(list.get(j).getFirst()) &&
                    list.get(i).getSecond().equals(list.get(j).getSecond())){
                    list.remove(j);
                    j--;
                }
            }
        }
    }
    //将结果输出到Data/greibach_output.txt中
    public void writeToTxt(List<ContextFreeNode> list){
        try {
            FileWriter output = new FileWriter("./Data/greibach_output.txt");
            //filewrite 到output.txt中
            @SuppressWarnings("resource")
            BufferedWriter bf = new BufferedWriter(output);

            for (int i = 0; i < list.size();i++) {
                bf.write(list.get(i).getFirst() + " -> " + list.get(i).getSecond() + "\n");
            }
            bf.flush();// 此处很关键，如果不写该语句，是不能从缓冲区写到文件里的
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //转换为格里巴克范式
    public void changeToGreibach_total(String filename) throws IOException {
        List<ContextFreeNode> list = new LinkedList<>();
        list = readRule(list,filename);
        judgeContextFreeGrammar(list);
        showRules(list);
        removeUselessSign(list);
        showRules(list);
        removeE(list);
        showRules(list);
        removeUnitProduction(list);
        removeUselessSign(list);
        showRules(list);
        System.out.println("消除直接左递归！");
        removeLeftRecursive(list);
        showRules(list);
        greibachFirstStep(list);
        removeUselessSign(list);
        showRules(list);
        //removeE(list);
        greibachSecondStep(list);
        showRules(list);
        writeToTxt(list);
    }

    public static void main(String[] args) throws IOException {
        ChangeToGreibach changeToGreibach = new ChangeToGreibach();
        changeToGreibach.changeToGreibach_total("./Data/Grammar7.txt");
    }
}
