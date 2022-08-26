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
        Post post1 = new Post(0, "один");
        Post post2 = new Post(0, "два");
        Post post3 = new Post(0, "три");
        Post post4 = new Post(0, "четыре");
        Post post5 = new Post(0, "пять");
        Post post6 = new Post(0, "шесть");
        Post post7 = new Post(0, "семь");
        Post post8 = new Post(0, "восемь");

        save(post1);
        save(post2);
        save(post3);
        removeById(5);
        removeById(4);
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
                    usedId.add(post.getId());                                                //добавляем newId в список использованных id
                }
            }
        }

        if (posts.size() != 0) {
            if (post.getId() == 0) {                               //если список постов не пустой и пришел пост с id=0
                if (!freeId.isEmpty()) {
                    post.setId(freeId.stream().min(Comparator.naturalOrder()).get());
                    freeId.remove(post.getId());
                    posts.add(post);
                    usedId.add(post.getId());
                } else {
                    while (usedId.contains(newId.get())) {
                        newId.incrementAndGet();
                    }
                    post.setId(newId.get());
                    posts.add(post);
                    usedId.add(newId.get());
                }
            } else {
                if (usedId.contains(post.getId())) {
                    for (Post post1 : posts) {
                        if (post1.getId() == post.getId() && !post1.getContent().equals(post.getContent())) {
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