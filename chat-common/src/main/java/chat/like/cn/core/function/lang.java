package chat.like.cn.core.function;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * lang.
 *
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 16:46
 */
public interface lang {

    /**
     * 原型设计期间使用的实现的临时替代品
     *
     * @return {@link T}
     */
    static <T> T todo() {
        throw new RuntimeException("todo");
    }

    static <T> T todo(final String msg) {
        throw new RuntimeException(msg);
    }

    /**
     * 数组中是否包含元素
     */
    static <T> boolean contains(T[] array, T value) {
        for (T t : array) {
            if (eq(t, value)) return true;
        }
        return false;
    }

    /**
     * 判断两个对象是否相等
     */
    static boolean eq(Object a, Object b) {
        if (a instanceof BigDecimal a1 && b instanceof BigDecimal b1) {
            if (a1.equals(b1)) {
                return true;
            }
            return 0 == a1.compareTo(b1);
        }
        return Objects.equals(a, b);
    }

    /**
     * 如果 value 为null 则返回defVal
     */
    static <T> T defVal(T value, T defVal) {
        if (Objects.isNull(value)) {
            return defVal;
        }
        return value;
    }

    /**
     * 如果 value 为null 则返回defVal
     */
    static String defVal(String value, String defVal) {
        if (StrUtil.isBlank(value)) {
            return defVal;
        }
        return value;
    }

    /**
     * 格式化，占位符使用{}
     */
    static String format(String template, Object... params) { return StrUtil.format(template,params); }

    /**
     * 将 {@link Date} 转换为unix 时间戳
     *
     * @param date 日期
     * @return long
     */
    static long toUnix(final Date date) {
        return date.getTime() / 1000;
    }

    /**
     * 将 {@link LocalDateTime} 转换为unix 时间戳
     *
     * @param localDateTime 日期
     * @return long
     */
    static long toUnix(final LocalDateTime localDateTime) {
        return LocalDateTimeUtil.toEpochMilli(localDateTime) / 1000;
    }

    /**
     * 将unix 时间戳 转换为 {@link Date}
     *
     * @param unix unix 时间戳
     * @return Date
     */
    static Date fromUnix(final long unix) {
        return new Date(unix * 1000);
    }

    /**
     * 将unix 时间戳 转换为 {@link LocalDateTime}
     *
     * @param unix unix 时间戳
     * @return LocalDateTime
     */
    static LocalDateTime fromUnix2(final long unix) {
        return LocalDateTimeUtil.of(unix);
    }

    static <E> List<E> listOf() {
        return new ArrayList<E>();
    }

    static <E> List<E> listOf(final Stream<E> stream) {
        return stream.collect(Collectors.toList());
    }

    static <E> List<E> listOf(final E element) {
        final List<E> list = new ArrayList<>(1);
        list.add(element);
        return list;
    }

    static <E> List<E> listOf(final E e1, final E e2) {
        final List<E> list = new ArrayList<>(2);
        list.add(e1);
        list.add(e2);
        return list;
    }

    static <E> List<E> listOf(final E e1, final E e2, final E e3) {
        final List<E> list = new ArrayList<>(3);
        list.add(e1);
        list.add(e2);
        list.add(e3);
        return list;
    }

    static <E> List<E> listOf(final E e1, final E e2, final E e3, final E e4) {
        final List<E> list = new ArrayList<>(4);
        list.add(e1);
        list.add(e2);
        list.add(e3);
        list.add(e4);
        return list;
    }

    static <E> List<E> listOf(final E e1, final E e2, final E e3, final E e4, final E e5) {
        final List<E> list = new ArrayList<>(5);
        list.add(e1);
        list.add(e2);
        list.add(e3);
        list.add(e4);
        list.add(e5);
        return list;
    }

    static <E> List<E> listOf(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6) {
        final List<E> list = new ArrayList<>(6);
        list.add(e1);
        list.add(e2);
        list.add(e3);
        list.add(e4);
        list.add(e5);
        list.add(e6);
        return list;
    }


    static <E> List<E> listOf(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6, final E e7) {
        final List<E> list = new ArrayList<>(7);
        list.add(e1);
        list.add(e2);
        list.add(e3);
        list.add(e4);
        list.add(e5);
        list.add(e6);
        list.add(e7);
        return list;
    }

    static <E> List<E> listOf(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6, final E e7, final E e8) {
        final List<E> list = new ArrayList<>(8);
        list.add(e1);
        list.add(e2);
        list.add(e3);
        list.add(e4);
        list.add(e5);
        list.add(e6);
        list.add(e7);
        list.add(e8);
        return list;
    }

    @SafeVarargs
    static <E> List<E> listOf(final E... elements) {
        return ListUtil.list(false, elements);
    }

    static <E> LinkedList<E> linkedListOf() {
        return new LinkedList<>();
    }

    static <E> LinkedList<E> linkedListOf(final E element) {
        final LinkedList<E> list = new LinkedList<>();
        list.add(element);
        return list;
    }

    static <E> LinkedList<E> linkedListOf(final E e1, final E e2) {
        final LinkedList<E> list = new LinkedList<>();
        list.add(e1);
        list.add(e2);
        return list;
    }

    static <E> LinkedList<E> linkedListOf(final E e1, final E e2, final E e3) {
        final LinkedList<E> list = new LinkedList<>();
        list.add(e1);
        list.add(e2);
        list.add(e3);
        return list;
    }

    static <E> LinkedList<E> linkedListOf(final E e1, final E e2, final E e3, final E e4) {
        final LinkedList<E> list = new LinkedList<>();
        list.add(e1);
        list.add(e2);
        list.add(e3);
        list.add(e4);
        return list;
    }

    static <E> LinkedList<E> linkedListOf(final E e1, final E e2, final E e3, final E e4, final E e5) {
        final LinkedList<E> list = new LinkedList<>();
        list.add(e1);
        list.add(e2);
        list.add(e3);
        list.add(e4);
        list.add(e5);
        return list;
    }

    static <E> LinkedList<E> linkedListOf(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6) {
        final LinkedList<E> list = new LinkedList<>();
        list.add(e1);
        list.add(e2);
        list.add(e3);
        list.add(e4);
        list.add(e5);
        list.add(e6);
        return list;
    }

    static <E> LinkedList<E> linkedListOf(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6, final E e7) {
        final LinkedList<E> list = new LinkedList<>();
        list.add(e1);
        list.add(e2);
        list.add(e3);
        list.add(e4);
        list.add(e5);
        list.add(e6);
        list.add(e7);
        return list;
    }

    static <E> LinkedList<E> linkedListOf(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6, final E e7, final E e8) {
        final LinkedList<E> list = new LinkedList<>();
        list.add(e1);
        list.add(e2);
        list.add(e3);
        list.add(e4);
        list.add(e5);
        list.add(e6);
        list.add(e7);
        list.add(e8);
        return list;
    }

    @SafeVarargs
    static <E> LinkedList<E> linkedListOf(final E... element) {
        return ListUtil.toLinkedList(element);
    }

    static <E> Set<E> setOf() {
        return new HashSet<>();
    }

    static <E> Set<E> setOf(final Stream<E> stream) {
        return stream.collect(Collectors.toSet());
    }

    static <E> Set<E> setOf(final E element) {
        final Set<E> list = new HashSet<>(1);
        list.add(element);
        return list;
    }

    static <E> Set<E> setOf(final E e1, final E e2) {
        final Set<E> list = new HashSet<>(2);
        list.add(e1);
        list.add(e2);
        return list;
    }

    static <E> Set<E> setOf(final E e1, final E e2, final E e3) {
        final Set<E> list = new HashSet<>(3);
        list.add(e1);
        list.add(e2);
        list.add(e3);
        return list;
    }

    static <E> Set<E> setOf(final E e1, final E e2, final E e3, final E e4) {
        final Set<E> list = new HashSet<>(4);
        list.add(e1);
        list.add(e2);
        list.add(e3);
        list.add(e4);
        return list;
    }

    static <E> Set<E> setOf(final E e1, final E e2, final E e3, final E e4, final E e5) {
        final Set<E> list = new HashSet<>(5);
        list.add(e1);
        list.add(e2);
        list.add(e3);
        list.add(e4);
        list.add(e5);
        return list;
    }

    static <E> Set<E> setOf(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6) {
        final Set<E> list = new HashSet<>(6);
        list.add(e1);
        list.add(e2);
        list.add(e3);
        list.add(e4);
        list.add(e5);
        list.add(e6);
        return list;
    }


    static <E> Set<E> setOf(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6, final E e7) {
        final Set<E> list = new HashSet<>(7);
        list.add(e1);
        list.add(e2);
        list.add(e3);
        list.add(e4);
        list.add(e5);
        list.add(e6);
        list.add(e7);
        return list;
    }

    static <E> Set<E> setOf(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6, final E e7, final E e8) {
        final Set<E> list = new HashSet<>(8);
        list.add(e1);
        list.add(e2);
        list.add(e3);
        list.add(e4);
        list.add(e5);
        list.add(e6);
        list.add(e7);
        list.add(e8);
        return list;
    }

    @SafeVarargs
    static <E> Set<E> setOf(final E... elements) {
        return CollUtil.set(false, elements);
    }

    static <E> Set<E> linkedSetOf() {
        return new LinkedHashSet<>();
    }

    static <E> Set<E> linkedSetOf(final E element) {
        final Set<E> list = new LinkedHashSet<>(1);
        list.add(element);
        return list;
    }

    static <E> Set<E> linkedSetOf(final E e1, final E e2) {
        final Set<E> list = new LinkedHashSet<>(2);
        list.add(e1);
        list.add(e2);
        return list;
    }

    static <E> Set<E> linkedSetOf(final E e1, final E e2, final E e3) {
        final Set<E> list = new LinkedHashSet<>(3);
        list.add(e1);
        list.add(e2);
        list.add(e3);
        return list;
    }

    static <E> Set<E> linkedSetOf(final E e1, final E e2, final E e3, final E e4) {
        final Set<E> list = new LinkedHashSet<>(4);
        list.add(e1);
        list.add(e2);
        list.add(e3);
        list.add(e4);
        return list;
    }

    static <E> Set<E> linkedSetOf(final E e1, final E e2, final E e3, final E e4, final E e5) {
        final Set<E> list = new LinkedHashSet<>(5);
        list.add(e1);
        list.add(e2);
        list.add(e3);
        list.add(e4);
        list.add(e5);
        return list;
    }

    static <E> Set<E> linkedSetOf(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6) {
        final Set<E> list = new LinkedHashSet<>(6);
        list.add(e1);
        list.add(e2);
        list.add(e3);
        list.add(e4);
        list.add(e5);
        list.add(e6);
        return list;
    }

    static <E> Set<E> linkedSetOf(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6, final E e7) {
        final Set<E> list = new LinkedHashSet<>(7);
        list.add(e1);
        list.add(e2);
        list.add(e3);
        list.add(e4);
        list.add(e5);
        list.add(e6);
        list.add(e7);
        return list;
    }

    static <E> Set<E> linkedSetOf(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6, final E e7, final E e8) {
        final Set<E> list = new LinkedHashSet<>(8);
        list.add(e1);
        list.add(e2);
        list.add(e3);
        list.add(e4);
        list.add(e5);
        list.add(e6);
        list.add(e7);
        list.add(e8);
        return list;
    }

    @SafeVarargs
    static <E> Set<E> linkedSetOf(final E... elements) {
        return CollUtil.set(true, elements);
    }

    /**
     * 展开一个map -> K,V,K,V,K,V,K,V
     *
     * @param map map集合
     * @return {@link Object[] }
     */
    static <K, V> Object[] flat(final Map<K, V> map) {
        return map.entrySet().stream()
                .flatMap(e -> Stream.of(e.getKey(), e.getValue())).toArray();
    }

    static <K, V> Map<K, V> mapOf() {
        return new HashMap<>();
    }

    static <E, K, V> Map<K, V> mapOf(final Stream<E> stream,
                                     final Function<? super E, ? extends K> keyMapper,
                                     final Function<? super E, ? extends V> valueMapper) {
        return stream.collect(Collectors.toMap(keyMapper, valueMapper));
    }

    static <K, V> Map<K, V> mapOf(@NonNull final List<K> keys, @NonNull final List<V> values) {
        if (NumberUtil.equals(keys.size(), values.size())) {
            throw new IllegalArgumentException(format("Func mapOf error : keys size :{},values size:{}; please check ", keys.size(), values.size()));
        }
        final Map<K, V> map = new HashMap<>();

        for (int i = 0; i < keys.size(); i++) {
            map.put(keys.get(i), values.get(i));
        }
        return map;
    }


    static <K, V> Map<K, V> mapOf(final K key, final V val) {
        final Map<K, V> map = new HashMap<>(1);
        map.put(key, val);
        return map;
    }

    static <K, V> Map<K, V> mapOf(final K k1, final V v1, final K k2, final V v2) {
        final Map<K, V> map = new HashMap<>(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    static <K, V> Map<K, V> mapOf(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3) {
        final Map<K, V> map = new HashMap<>(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    static <K, V> Map<K, V> mapOf(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4) {
        final Map<K, V> map = new HashMap<>(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    static <K, V> Map<K, V> mapOf(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5,
                                  final V v5) {
        final Map<K, V> map = new HashMap<>(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    static <K, V> Map<K, V> mapOf(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5,
                                  final V v5, final K k6, final V v6) {
        final Map<K, V> map = new HashMap<>(6);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        return map;
    }

    static <K, V> Map<K, V> mapOf(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5,
                                  final V v5, final K k6, final V v6, final K k7, final V v7) {
        final Map<K, V> map = new HashMap<>(6);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        return map;
    }

    static <K, V> Map<K, V> mapOf(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5,
                                  final V v5, final K k6, final V v6, final K k7, final V v7, final K k8, final V v8) {
        final Map<K, V> map = new HashMap<>(6);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        return map;
    }

    static <K, V> Map<K, V> mapOf(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5,
                                  final V v5, final K k6, final V v6, final K k7, final V v7, final K k8, final V v8, final K k9, final V v9) {
        final Map<K, V> map = new HashMap<>(6);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        return map;
    }

    static <K, V> Map<K, V> mapOf(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5,
                                  final V v5, final K k6, final V v6, final K k7, final V v7, final K k8, final V v8, final K k9, final V v9,
                                  final K k10, final V v10) {
        final Map<K, V> map = new HashMap<>(6);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        return map;
    }


    static <K, V> Map<K, V> cMapOf() {
        return new ConcurrentHashMap<>();
    }

    static <K, V> Map<K, V> cMapOf(final K key, final V val) {
        final Map<K, V> map = new ConcurrentHashMap<>(1);
        map.put(key, val);
        return map;
    }

    static <K, V> Map<K, V> cMapOf(final K k1, final V v1, final K k2, final V v2) {
        final Map<K, V> map = new ConcurrentHashMap<>(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    static <K, V> Map<K, V> cMapOf(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3) {
        final Map<K, V> map = new ConcurrentHashMap<>(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    static <K, V> Map<K, V> cMapOf(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4) {
        final Map<K, V> map = new ConcurrentHashMap<>(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    static <K, V> Map<K, V> cMapOf(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5,
                                   final V v5) {
        final Map<K, V> map = new ConcurrentHashMap<>(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    static <K, V> Map<K, V> cMapOf(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5,
                                   final V v5, final K k6, final V v6) {
        final Map<K, V> map = new ConcurrentHashMap<>(6);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        return map;
    }

    static <E> E[] toArray(final Collection<E> collection, final Class<E> clazz) {
        return ArrayUtil.toArray(collection, clazz);
    }

    /**
     * 将集合转换为hashSet
     */
    static Set<String> toSet(final Collection<String> elements) {
        if (elements instanceof Set) return (Set<String>) elements;
        return new HashSet<>(elements);
    }

    /**
     * 将 {@link Stream} 收集为 {@link ArrayList}
     */
    static <T> List<T> toList(final Stream<T> stream) {
        return stream.collect(Collectors.toList());
    }

    /**
     * 分组
     */
    static <T, K> Map<K, List<T>> groupBy(final Collection<T> list, final Function<? super T, K> classifier) {
        return list.stream().collect(Collectors.groupingBy(classifier));
    }

    /**
     * 将 {@link Stream} 收集为 {@link HashSet}
     */
    static <T> Set<T> toSet(final Stream<T> stream) {
        return stream.collect(Collectors.toSet());
    }

    /**
     * 求交集
     */
    static <E> List<E> intersection(final Collection<E> c1, final Collection<E> c2) {
        return (List<E>) CollectionUtil.intersection(c1, c2);
    }

    /**
     * 求差集
     */
    static <T> List<T> disjunction(final List<T> l1, final List<T> l2) {
        return (List<T>) CollUtil.disjunction(l1, l2);
    }
}