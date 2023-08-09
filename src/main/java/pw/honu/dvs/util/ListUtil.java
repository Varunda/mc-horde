package pw.honu.dvs.util;

import java.util.List;
import java.util.Random;

public class ListUtil {

    private static final Random random = new Random();

    public static <T> T getRandomElement(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }

}
