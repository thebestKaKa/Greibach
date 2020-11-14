import java.util.*;

public class Test {
    public void removeE_2(List<ContextFreeNode> list){
        Iterator<ContextFreeNode> iterator = list.iterator();
        Set<ContextFreeNode> set = new HashSet<>();
        while (iterator.hasNext()){
            ContextFreeNode node_temp = iterator.next();
            char[] arr = node_temp.getSecond().toCharArray();
            //如果S->e 暂未处理
            if (arr[0] == '$'){
                System.out.println("Remove $!");
                iterator.remove();
                char V = node_temp.getFirst().charAt(0);
                String[] production_right = new String[50];
                int count = 0;
                // 获取推空非终右端字符串
                for (ContextFreeNode node:list){
                    if (node.getFirst().charAt(0) == V)
                        production_right[count++] = node.getSecond();
                }
                //replaceV(list,V,production_right,set);
            }
            for(ContextFreeNode node:set){
                list.add(node);
                set.clear();
            }
        }
    }
    //消除无用符号
    public void removeUselessSign_2(List<ContextFreeNode> list){
        System.out.println("消除无用符号");
        int[] isUseful = new int[26];
        int[] canFinish = new int[26];
        for (int count = 0; count < list.size();count++){
            ContextFreeNode node = list.get(count);
            //推出自己
            if (node.getFirst().equals(node.getSecond())){
                list.remove(count);
                count--;
                continue;
            }
            char left = node.getFirst().charAt(0);
            char[] right = node.getSecond().toCharArray();
            isUseful[left - 'A'] = 1;
            if (right.length == 1 && Character.isLowerCase(right[0]))
                canFinish[node.getFirst().charAt(0) - 'A'] = 1;
        }
        for (int count1 = 0; count1 < list.size();count1++){
            ContextFreeNode n = list.get(count1);
            //没有用到
            char[] right = n.getSecond().toCharArray();
            for(int count2 = 0; count2 < right.length;count2++){
                if (Character.isUpperCase(right[count2])){
                    if (isUseful[right[count2] - 'A'] == 0){
                        list.remove(count1);
                        count1--;
                        break;
                    }
                }
            }
        }
        //不能终止
//        for (int count3 = 0; count3 < list.size();count3++){
//            ContextFreeNode node = list.get(count3);
//            char left = node.getFirst().charAt(0);
//            if (canFinish[left - 'A'] == 0){
//                char[] right = node.getSecond().toCharArray();
//                HashSet<Character> upper = new HashSet<>();
//                for (int count4 = 0; count4 < right.length;count4++){
//                    if (Character.isUpperCase(right[count4]) )
//                        upper.add(right[count4]);
//                }
//                for ()
//            }
//        }
    }
    //消除非终推出一个终结符且该非终只有这一个产生式
    public void removeFirst(List<ContextFreeNode> list){
        System.out.println("消除无用符号");
        int[] countV = new int[26];
        for (ContextFreeNode node:list)countV[node.getFirst().charAt(0) - 'A']++;
        for (int i = 0; i < list.size();i++){
            ContextFreeNode node = list.get(i);
            char V = node.getFirst().charAt(0);
            if (countV[V - 'A'] == 1){
                char[] right = node.getSecond().toCharArray();
                if (right.length == 1){
                    char t = right[0];
                    if (Character.isLowerCase(t)){
                        list.remove(i);
                        i--;
                        for(ContextFreeNode n:list){
                            StringBuilder str = new StringBuilder(n.getSecond());
                            char[] arr = n.getSecond().toCharArray();
                            for(int c = 0;c < arr.length;c++){
                                if (arr[c] == V){
                                    str.deleteCharAt(c);
                                    str.insert(c, t);
                                }
                            }
                            n.setSecond(str.toString());
                        }
                    }
                    if(Character.isUpperCase(right[0])){
                        list.remove(i);
                        i--;
                        char T = t;
                        countV[V - 'A'] = countV[T - 'A'];
                        for (ContextFreeNode node1:list){
                            if (node1.getFirst().charAt(0) == T)
                                node1.setFirst(String.valueOf(V));
                            StringBuilder str1 = new StringBuilder(node1.getSecond());
                            char[] arr = node1.getSecond().toCharArray();
                            for (int d = 0; d < arr.length; d++){
                                if (arr[d] == T){
                                    str1.deleteCharAt(d);
                                    str1.insert(d, V);
                                }
                            }
                            node1.setSecond(str1.toString());
                        }
                    }
                }
            }
        }
    }

//    public boolean isCpmpared(List<ContextFreeNode> list,Stack<Character> stk,char[] str_input){
//        //当字符串结束且栈为空时匹配成功
//        if (count == str_input.length && stk.isEmpty()) return true;
//        if (count == str_input.length && !stk.isEmpty())return false;
//        Stack<Character> stk_pre = (Stack<Character>) stk.clone();
//
//
//        for (int i = 0; i < list.size(); i++){
//            if (count < str_input.length && stk.isEmpty()) return false;
//            ContextFreeNode node = list.get(i);
//            if (node.getFirst().charAt(0) == stk.peek() && node.getSecond().charAt(0) == str_input[count]){
//                stk.pop();
//                count++;
//                char[] right = node.getSecond().toCharArray();
//                for (int j = right.length - 1; j > 0; j--) stk.push(right[j]);
//                if(isCpmpared(list,stk,str_input)){
//                    System.out.println(stk_pre);
//                    System.out.println(stk);
//                    System.out.println(count);
//                    return true;
//                }
//                else {
//                    count--;
//                    System.out.println(stk_pre);
//                    System.out.println(stk);
//                    System.out.println(count);
//                    stk = stk_pre;//回溯
//                    continue;
//                }
//            }else continue;
//        }
//        //没找到
//        return false;
//    }

    public static void main(String[] args) {
        HashSet<Character> set = new HashSet<>();
        set.add('a');
        HashSet<Character> c = (HashSet<Character>) set.clone();
        c.add('b');
        set.add('c');
        System.out.println(set);
        System.out.println(c);
    }
    //check
//        for (int i = 0; i < list.size(); i++){
//            stk.clear();
//            count = 0;
//            ContextFreeNode node = list.get(i);
//            if (node.getFirst().equals("S") && node.getSecond().charAt(0) == str_input[count]){
//                //当字符串结束且栈为空时匹配成功
//                //if (count == str_input.length && stk.isEmpty())return true;
//                count++;
//                char[] right = node.getSecond().toCharArray();
//                //非终结符入栈 第一个已经匹配直接忽略
//                for (int j = right.length - 1; j > 0; j--) stk.push(right[j]);
//                if(isCpmpared(list,stk,str_input))return true;
//                else {
//                    count--;
//                    continue;
//                }
//            }
//        }
    //end check
}
//public class TuringMachine {
//    //计算x的y次方
//    public void computeYPowerOfX(int x, int y){
//        //初始化图灵带为y0x010
//        TuringNode s = new TuringNode();
//        s.setData('1');
//        //tail为尾节点
//        TuringNode tail = s;
//
//        for (int i = 0; i < y - 1;i++){
//            TuringNode p = new TuringNode();
//            p.setData('1');
//            p.pre = tail;
//            tail.next = p;
//            tail = p;
//        }
//
//        TuringNode p = new TuringNode();
//        p.setData('0');
//        p.pre = tail;
//        tail.next = p;
//        tail = p;
//
//        for (int i = 0; i < x;i++){
//            p = new TuringNode();
//            p.setData('1');
//            p.pre = tail;
//            tail.next = p;
//            tail = p;
//        }
//
//        p = new TuringNode();
//        p.setData('0');
//        p.pre = tail;
//        tail.next = p;
//        tail = p;
//
//        p = new TuringNode();
//        p.setData('1');
//        p.pre = tail;
//        tail.next = p;
//        tail = p;
//
//        p = new TuringNode();
//        p.setData('0');
//        p.pre = tail;
//        tail.next = p;
//        tail = p;
//        //初始化结束
//        //移动指针q
//        TuringNode q = s;
//        //状态转移
//        int state = 0;
//        while (state != 18){
//            switch (state){
//                case 0:
//                    if (q.data == '0'){
//                        state = 17;
//                        q = q.pre;
//                    }else if (q.data == '1'){
//                        state = 1;
//                        q.setData('a');
//                        q = q.next;
//                    }else state = 18;
//                    break;
//                case 1:
//                    if (q.data == '0'){
//                        state = 2;
//                        q = q.next;
//                    }else if (q.data == '1'){
//                        state = 1;
//                        q = q.next;
//                    }else state = 18;
//                    break;
//                case 2:
//                    if (q.data == '0'){
//                        state = 10;
//                        q = q.pre;
//                    }else if (q.data == '1'){
//                        state = 3;
//                        q.setData('b');
//                        q = q.next;
//                    }else state = 18;
//                    break;
//                case 3:
//                    if (q.data == '0'){
//                        state = 4;
//                        q = q.next;
//                    }else if (q.data == '1'){
//                        state = 3;
//                        q = q.next;
//                    }else state = 18;
//                    break;
//                case 4:
//                    if (q.data == '0'){
//                        state = 9;
//                        q = q.pre;
//                    }else if (q.data == '1'){
//                        state = 5;
//                        q.setData('c');
//                        q = q.next;
//                    }else state = 18;
//                    break;
//                case 5:
//                    if (q.data == '0'){
//                        state = 6;
//                        q = q.next;
//                    }else if (q.data == '1'){
//                        state = 5;
//                        q = q.next;
//                    }else state = 18;
//                    break;
//                //??
//                case 6:
//                    if(q == null){
//                        p = new TuringNode();
//                        p.setData('1');
//                        p.pre = tail;
//                        p.next = null;
//                        tail.next = p;
//                        tail = p;
//                        q = p;
//                        state = 7;
//                        q = q.next;
//                    }else if (q.data == '1'){
//                        state = 6;
//                        q = q.next;
//                    }else if (q.data == '0'){
//                        state = 7;
//                        q.setData('1');
//                        q = q.next;
//                    }else state = 18;
//                    break;
//                case 7:
//                    if(q == null){
//                        p = new TuringNode();
//                        p.setData('0');
//                        p.pre = tail;
//                        p.next = null;
//                        tail.next = p;
//                        tail = p;
//                        state = 8;
//                        q = tail.pre;
//                    }else state = 18;
//                    break;
//                case 8:
//                    if (q.data == '0'){
//                        state = 8;
//                        q = q.pre;
//                    }else if (q.data == '1'){
//                        state = 8;
//                        q = q.pre;
//                    }else if (q.data == 'c'){
//                        state = 4;
//                        q = q.next;
//                    }else state = 18;
//                    break;
//                case 9:
//                    if (q.data == '0'){
//                        state = 9;
//                        q = q.pre;
//                    }else if (q.data == '1'){
//                        state = 9;
//                        q = q.pre;
//                    }else if (q.data == 'b'){
//                        state = 2;
//                        q = q.next;
//                    }else if (q.data == 'c'){
//                        state = 9;
//                        q.setData('1');
//                        q = q.pre;
//                    }else state = 18;
//                    break;
//                case 10:
//                    if (q.data == '0'){
//                        state = 11;
//                        q = q.next;
//                    }else if (q.data == 'b'){
//                        state = 10;
//                        q.setData('1');
//                        q = q.pre;
//                    }else state = 18;
//                    break;
//                case 11:
//                    if (q.data == '0'){
//                        state = 12;
//                        q = q.next;
//                    }else if (q.data == '1'){
//                        state = 11;
//                        q = q.next;
//                    }else state = 18;
//                    break;
//                case 12:
//                    if (q.data == '0'){
//                        state = 13;
//                        q.setData('1');
//                        q = q.next;
//                    }else if (q.data == '1'){
//                        state = 12;
//                        q.setData('d');
//                        q = q.next;
//                    }else state = 18;
//                    break;
//                //???
//                case 13:
//                    if (q.data == '0'){
//                        state = 14;
//                        q = q.pre;
//                        tail = tail.pre;
//                        tail.next = null;
//                    }else if (q.data == '1'){
//                        state = 13;
//                        q = q.next;
//                    }else state = 18;
//                    break;
//                case 14:
//                    if (q.data == '1'){
//                        state = 15;
//                        q.setData('0');
//                        q = q.pre;
//                    }else state = 18;
//                    break;
//                case 15:
//                    if (q.data == '0'){
//                        state = 16;
//                        q = q.pre;
//                    }else if (q.data == '1'){
//                        state = 15;
//                        q = q.pre;
//                    }else if (q.data == 'd'){
//                        state = 13;
//                        q.setData('1');
//                        q = q.next;
//                    }else state = 18;
//                    break;
//                case 16:
//                    if (q.data == '0'){
//                        state = 16;
//                        q = q.pre;
//                    }else if (q.data == '1'){
//                        state = 16;
//                        q = q.pre;
//                    }else if (q.data == 'a'){
//                        state = 0;
//                        //q.setData('1');
//                        q = q.next;
//                    }else state = 18;
//                    break;
//                case 17:
//                    if (q == null){
//                        state = 18;
//                        q = s;
//                    }else if (q.data == 'a'){
//                        state = 17;
//                        q.setData('1');
//                        q = q.pre;
//                    }else state = 18;
//                    break;
//                default:
//                    break;
//            }
//            System.out.println("当前状态:" + state);
//            System.out.print("当前图灵带:");
//            TuringNode temp = s;
//            while (temp != null){
//                System.out.print(temp.data);
//                temp = temp.next;
//            }
//            System.out.println(" ");
//        }
//        int ans = 0;
//        TuringNode count = tail.pre;
//        while (count.data != '0'){
//            ans++;
//            count = count.pre;
//        }
//        System.out.println(ans);
//    }
//
//    public static void main(String[] args) {
//        TuringMachine tm = new TuringMachine();
//        tm.computeYPowerOfX(2, 3);
//    }
//}
