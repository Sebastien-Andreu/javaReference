package code.utils;

import code.singleton.SingletonFileSelected;


public class BibTexFormat {

    public static String getAuthor(){
        String list = SingletonFileSelected.getInstance().file.firstMap.get("author").replace("[", "").replace("]", "").replace("\"", "");
        String[] ary = list.split(",");
        String author = "";
        for (int i = 0; i < ary.length ; i++) {
            String[] name = ary[i].replace(",", "").split(" ", 2);
            if (name.length == 1){
                try{
                    author += ary[i] + ", Surname Missing !!";
                } catch (Exception e){
                    System.out.println("1 : "  + e.getMessage());
                }
            } else {
                try{
                    name[0] = name[0].substring(0, 1).toUpperCase() + name[0].substring(1);
                    name[1] = name[1].substring(0, 1).toUpperCase() + name[1].substring(1);
                    author += name[0]+ ", "+ name[1];
                } catch (Exception e){
                    System.out.println("2 : "  + e.getMessage());
                }
            }

            if (i < ary.length -1 ){
                author += " and ";
            }

        }
        return author;
    }
}
