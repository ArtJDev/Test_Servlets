import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicLong;

public class Main {
    private static AtomicLong newId = new AtomicLong();                 //хранит id по порядку
    private static Set<Long> usedId = new CopyOnWriteArraySet<>();      //хранит список использованных id
    private static Set<Long> freeId = new CopyOnWriteArraySet<>();      //хранит список свободных id
    private static final List<Post> posts = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        Post post1 = new Post(10, "один");
        Post post2 = new Post(0, "два");
        Post post3 = new Post(0, "три");
        Post post4 = new Post(0, "четыре");
        Post post5 = new Post(0, "пять");
        Post post6 = new Post(0, "шесть");
        Post post7 = new Post(0, "семь");
        Post post8 = new Post(10, "восемь");

        save(post1);
        save(post2);
        save(post3);
        save(post4);
        save(post5);
        save(post6);
        save(post7);
        save(post8);


        for (Post post : posts) {
            System.out.println(post.getId() + " " + post.getContent());
        }
        for (Long aLong : freeId) {
            System.out.println(aLong);
        }

    }

    public static void save(Post post) {
        if (newId.get() == 0) {         //сразу присваиваем newId = 1
            newId.set(1);
        }

        if (posts.size() == 0) {            //если список постов пустой, то
            if (post.getId() == 0) {        //если пришел пост с id = 0
                post.setId(newId.get());    //присваиваем посту id = 1
                posts.add(post);            //и добавляем его в лист
                usedId.add(newId.get());    //добавляем newId в список использованных id
                newId.incrementAndGet();    //увеличиваем newId на 1
            } else {                        //иначе, если id поста != 0, то
                posts.add(post);            //просто добавляем пост с этим id в список постов
                usedId.add(post.getId());   //добавляем newId в список использованных id
            }
        } else {                            //иначе если список постов не пустой, то
            if (post.getId() == 0) {            //если пришел пост с id = 0, то
                if (!freeId.isEmpty()) {        //смотрим есть ли свободные номера для id, если да, то
                    post.setId(freeId.stream().min(Comparator.naturalOrder()).get());   //присваиваем посту минимальный id из списка свободных
                    freeId.remove(post.getId());    //удаляем номер id из списка свободных
                    posts.add(post);                //добавляем пост в список постов
                    usedId.add(post.getId());       //добавлем номер id в список использованных
                } else {                            //иначе, если свободных номеров для id нет, то
                    while (usedId.contains(newId.get())) {  //в цикле смотрим, содержится ли следующий по порядку id в списке использованных
                        newId.incrementAndGet();            //каждый раз увеличивая его на 1
                    }                                       //как только находится следующий не использованный по порядку номер id, выходим из цикла
                    post.setId(newId.get());        //присваиваем посту новый newId
                    posts.add(post);                //добавляем пост в список постов
                    usedId.add(newId.get());        //добавляем номер id в список использованных
                }
            } else {                            //иначе, если пришел пост с id != 0, то
                if (usedId.contains(post.getId())) {        //смотрим, есть ли уже такой id в списке использованных, если да, то
                    for (Post post1 : posts) {              //ищем пост с таким же id
                        if (post1.getId() == post.getId() && !post1.getContent().equals(post.getContent())) { //коогда найдется пост с таким же id, сравниваем контент, если контент разный, то
                            posts.remove(post1);            //удаляем старый пост из списка
                            posts.add(post);                //добавляем новый посто в список
                        }
                    }
                } else {                                    //иначе, если поста с таки id не существует, то
                    posts.add(post);                        //добавляем пост в список
                    usedId.add(post.getId());               //добавляем номер id поста в список использованных
                }
            }
        }
    }

    public static void removeById(long id) {
        if (usedId.contains(id)) {
            for (Post post1 : posts) {
                if (post1.getId() == id) {
                    posts.remove(post1);
                    usedId.remove(id);
                    freeId.add(id);
                }
            }
        } else {
            System.out.println("Пост не найден");
        }
    }
}