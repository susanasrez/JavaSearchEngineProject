import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.multimap.MultiMap;

public class Main {
    public static void main(String[] args) {
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
        HazelcastInstance client = HazelcastClient.newHazelcastClient();
        MultiMap<Object, Object> invertedIndex = client.getMultiMap("invertedIndex");

        for (Object key : invertedIndex.keySet()) {
            System.out.println((String) key + ": " + invertedIndex.get(key));
        }
    }
}
