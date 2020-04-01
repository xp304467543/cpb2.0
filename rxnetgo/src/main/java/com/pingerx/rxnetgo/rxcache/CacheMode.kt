package com.pingerx.rxnetgo.rxcache

/**
 * @author Pinger
 * @since 18-10-17 上午11:32
 *
 * 网络请求的缓存模式
 */
enum class CacheMode {

    /**
     * 不使用缓存策略。
     * 不会读取缓存，也不会保存缓存，只会使用网络数据。
     */
    NONE,

    /**
     * 只使用缓存，不会请求网络。
     * 如果没有缓存，会回调onFailure方法。
     */
    ONLY_CACHE,

    /**
     * 只请求网络，不使用缓存。请求成功后，会缓存数据，请求失败后，直接回调onFailure方法。
     */
    ONLY_REMOTE,

    /**
     * 优先使用缓存，只会回调一次onSuccess
     * 如果有缓存，读取缓存后回调onSuccess。
     * 如果没有缓存，请求网络数据后回调onSuccess，然后保存缓存。
     */
    FIRST_CACHE_NONE_REMOTE,


    /**
     * 优先使用缓存，只会回调一次onSuccess
     * 如果有缓存，读取缓存后回调onSuccess，然后请求网络数据进行缓存。
     * 如果没有缓存，请求网络数据后回调onSuccess，然后保存缓存。
     */
    FIRST_CACHE_THEN_REMOTE,

    /**
     * 优先发起网络请求数据，并且缓存数据。
     * 请求成功，缓存数据。
     * 请求失败，有缓存则展示缓存，无缓存则回调onFailure方法。
     */
    FIRST_REMOTE_THEN_CACHE,

    /**
     * 优先读取缓存，然后请求网络覆盖缓存，如果有缓存会覆盖两次。
     * 如果有缓存，回调onSuccess方法，然后请求网络数据，继续回调onSuccess方法。
     * 如果没有缓存，直接请求网络数据，回调onSuccess方法。
     */
    FIRST_CACHE_AND_REMOTE
}
