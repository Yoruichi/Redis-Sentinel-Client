package td.redis.sentinel.client;

import td.redis.sentinel.client.component.Sentinel;

/**
 * Created by apple on 14/12/3.
 */
public class TestBitMap {
    public static void main(String[] args) {
        Sentinel sentinel = new Sentinel("appcpa", "localhost:26379", "localhost:26380", "localhost:26381");
        RedisClient r = new RedisClient(sentinel);
        String key = "test";

        for (long i = 0; i < 100; i++) {
            r.setbit(key, i, true);
        }
        System.out.println(r.bitCount(key));

        String src1 = "src1";
        for (long i = 0; i < 120; i++) {
            r.setbit(src1, i, i % 2 == 0 ? true : false);
        }
        System.out.println("src1 count : " + r.bitCount(src1));

        String src2 = "src2";
        for (long i = 0; i < 120; i++) {
            r.setbit(src2, i, i % 2 == 0 ? false : true);
        }
        System.out.println("src2 count : " + r.bitCount(src2));

        String tgKey = "tgKey";
        r.bitAnd(tgKey,src1,src2);
        System.out.println("src1 and src2 --> count : " + r.bitCount(tgKey));

        r.bitOr(tgKey, src1, src2);
        System.out.println("src1 or src2 --> count : " + r.bitCount(tgKey));

        r.bitNot(tgKey,key);
        System.out.println("key test not --> count : " + r.bitCount(tgKey));
    }
}
