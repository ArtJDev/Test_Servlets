import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicLong;

public class Main {
    private static AtomicLong newId = new AtomicLong();
    private static Set<Long> usedId = new CopyOnWriteArraySet<>();
    private static Set<Long> freeId = new CopyOnWriteArraySet<>();
    private static final List<Post> posts = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        Post post1 = new Post(10, "один");
        Post post2 = new Post(10, "два");
        Post post3 = new Post(0, "три");
        Post post4 = new Post(2, "четыре");
        Post post5 = new Post(0, "пять");

        save(post1);
        save(post2);
        save(post3);
        save(post4);
        save(post5);

        for (Post post : posts) {
            System.out.println(post.getId() + " " + post.getContent());
        }

    }

    public static void save(Post post) {
        if (newId.get() == 0) {
            newId.set(1);
        }

        if (posts.size() == 0) {
            if (post.getId() == 0) {                               //если список постов пустой и пришел пост с id=0
                post.setId(newId.get());                                                          //то присваиваем посту id=1
                posts.add(post);                                                        //и добавляем его в лист
                usedId.add(newId.get());                                                //добавляем newId в список использованных id
                newId.incrementAndGet();
            } else {
                if (post.getId() != 0) {                               //если список постов пустой и пришел пост с id != 0
                    posts.add(post);                                                        //просто добавляем пост с этим id в список постов
//                    newId.set(post.getId());                                                //newId присваиваем id = id поста
                    usedId.add(post.getId());                                                //добавляем newId в список использованных id
                }
            }
        }

        if (posts.size() != 0) {
            if (post.getId() == 0) {                               //если список постов не пустой и пришел пост с id=0
                if (usedId.add(newId.get())) {                      //приходит с индексом 0, и проверяет что не содержится, ставит предыдущий id и идет дальше
                    post.setId(newId.get());
                } else {
                    post.setId(newId.incrementAndGet());
                }
                posts.add(post);
                usedId.add(newId.get());
            } else {
                if (usedId.contains(post.getId())) {
                    for (Post post1 : posts) {
                        if (post1.getId() == post.getId()) {
                            posts.remove(post1);
                            posts.add(post);
                        }
                    }
                } else {
                    usedId.add(post.getId());
                    posts.add(post);
                }
            }
        }
    }

    public void removeById(long id) {

    }
}