package com.pingerx.rxnetgo.subscribe.base

import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.ListCompositeDisposable
import io.reactivex.internal.functions.ObjectHelper
import io.reactivex.internal.subscriptions.SubscriptionHelper
import io.reactivex.internal.util.EndConsumerHelper
import org.reactivestreams.Subscription
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference

/**
 * An abstract Subscriber that allows asynchronous cancellation of its
 * subscription and associated resources.
 *
 *
 * All pre-implemented final methods are thread-safe.
 *
 *
 * To release the associated resources, one has to call [.dispose]
 * in `onError()` and `onComplete()` explicitly.
 *
 *
 * Use [.add] to associate resources (as [Disposable]s)
 * with this `ResourceSubscriber` that will be cleaned up when [.dispose] is called.
 * Removing previously associated resources is not possible but one can create a
 * [CompositeDisposable][io.reactivex.disposables.CompositeDisposable], associate it with this
 * `ResourceSubscriber` and then add/remove resources to/from the `CompositeDisposable`
 * freely.
 *
 *
 * The default [.onStart] requests Long.MAX_VALUE by default. Override
 * the method to request a custom *positive* amount. Use the protected [.request]
 * to request more items and [.dispose] to cancel the sequence from within an
 * `onNext` implementation.
 *
 *
 * Note that calling [.request] from [.onStart] may trigger
 * an immediate, asynchronous emission of data to [.onNext]. Make sure
 * all initialization happens before the call to `request()` in `onStart()`.
 * Calling [.request] inside [.onNext] can happen at any time
 * because by design, `onNext` calls from upstream are non-reentrant and non-overlapping.
 *
 *
 * Like all other consumers, `ResourceSubscriber` can be subscribed only once.
 * Any subsequent attempt to subscribe it to a new source will yield an
 * [IllegalStateException] with message `"It is not allowed to subscribe with a(n) <class name> multiple times."`.
 *
 *
 * Implementation of [.onStart], [.onNext], [.onError]
 * and [.onComplete] are not allowed to throw any unchecked exceptions.
 * If for some reason this can't be avoided, use [io.reactivex.Flowable.safeSubscribe]
 * instead of the standard `subscribe()` method.
 *
 *
 * Example`<pre>
 * Disposable d =
 * Flowable.range(1, 5)
 * .subscribeWith(new ResourceSubscriber&lt;Integer>() {
 * &#64;Override public void onStart() {
 * add(Schedulers.single()
 * .scheduleDirect(() -> System.out.println("Time!"),
 * 2, TimeUnit.SECONDS));
 * request(1);
 * }
 * &#64;Override public void onNext(Integer t) {
 * if (t == 3) {
 * dispose();
 * }
 * System.out.println(t);
 * request(1);
 * }
 * &#64;Override public void onError(Throwable t) {
 * t.printStackTrace();
 * dispose();
 * }
 * &#64;Override public void onComplete() {
 * System.out.println("Done!");
 * dispose();
 * }
 * });
 * // ...
 * d.dispose();
</pre>` *
 *
 *
 * 将onStart()方法中的request()方法移到onSubscribe()中调用
 * 防止调用[onStart]时移除[super().onStart]导致发不出请求
 *
 * @param <T> the value type
</T> */
abstract class ResourceSubscriber<T> : io.reactivex.FlowableSubscriber<T>, io.reactivex.disposables.Disposable {
    /**
     * The active subscription.
     */
    private val s = AtomicReference<Subscription>()

    /**
     * The resource composite, can never be null.
     */
    private val resources = ListCompositeDisposable()

    /**
     * Remembers the request(n) counts until a subscription arrives.
     */
    private val missedRequested = AtomicLong()

    /**
     * Adds a resource to this AsyncObserver.
     *
     * @param resource the resource to add
     * @throws NullPointerException if resource is null
     */
    fun add(resource: Disposable) {
        ObjectHelper.requireNonNull(resource, "resource is null")
        resources.add(resource)
    }

    final override fun onSubscribe(s: Subscription) {
        if (EndConsumerHelper.setOnce(this.s, s, javaClass)) {
            onStart()
            val r = missedRequested.getAndSet(0L)
            if (r != 0L) {
                s.request(r)
            }
            request(java.lang.Long.MAX_VALUE)
        }
    }

    /**
     * Called once the upstream sets a Subscription on this AsyncObserver.
     *
     *
     * You can perform initialization at this moment. The default
     * implementation requests Long.MAX_VALUE from upstream.
     */
    protected open fun onStart() {}

    /**
     * Request the specified amount of elements from upstream.
     *
     *
     * This method can be called before the upstream calls onSubscribe().
     * When the subscription happens, all missed requests are requested.
     *
     * @param n the request amount, must be positive
     */
    protected fun request(n: Long) {
        SubscriptionHelper.deferredRequest(s, missedRequested, n)
    }

    /**
     * Cancels the subscription (if any) and disposes the resources associated with
     * this AsyncObserver (if any).
     *
     *
     * This method can be called before the upstream calls onSubscribe at which
     * case the Subscription will be immediately cancelled.
     */
    final override fun dispose() {
        if (SubscriptionHelper.cancel(s)) {
            resources.dispose()
        }
    }

    /**
     * Returns true if this AsyncObserver has been disposed/cancelled.
     *
     * @return true if this AsyncObserver has been disposed/cancelled
     */
    final override fun isDisposed(): Boolean {
        return resources.isDisposed
    }
}
