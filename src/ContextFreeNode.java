public class ContextFreeNode {
    //存放上下文无关文法产生式
    //first存放左边非终结符
    //second存放右边部分 $表示空
    private String first;
    private String second;

    public String getFirst() {
        return first;
    }
    public String getSecond() {
        return second;
    }
    public void setFirst(String first) {
        this.first = first;
    }
    public void setSecond(String second) {
        this.second = second;
    }
}
