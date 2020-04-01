package com.fenghuang.caipiaobao.base

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.test.rule.ActivityTestRule
import com.fenghuang.caipiaobao.test.FragmentTestActivity


/**
 * This rule provides functional testing of a single fragment.
 *
 *
 * Idea extracted from: http://stackoverflow.com/a/38393087/842697
 *
 * @param <A> The container activity for the fragment under test
 * @param <F> The fragment under test
</F></A> */
class FragmentTestRule<A : FragmentTestActivity, F : Fragment>
/**
 * Creates an [FragmentTestRule] for the Fragment under test.
 *
 * @param activityClass    The container activity for the fragment under test. This must be a
 * class in the instrumentation targetPackage specified in the
 * AndroidManifest.xml
 * @param fragmentClass    The fragment under test
 * @param initialTouchMode true if the Activity should be placed into "touch mode" when started
 * @param launchActivity   true if the Activity should be launched once per
 * [
 * `Test`](http://junit.org/javadoc/latest/org/junit/Test.html) method. It will be launched before the first
 * [
 * `Before`](http://junit.sourceforge.net/javadoc/org/junit/Before.html) method, and terminated after the last
 * [
 * `After`](http://junit.sourceforge.net/javadoc/org/junit/After.html) method.
 * @param launchFragment   true if the Fragment should be launched once per
 * [
 * `Test`](http://junit.org/javadoc/latest/org/junit/Test.html) method. It will be launched before the first
 * [
 * `Before`](http://junit.sourceforge.net/javadoc/org/junit/Before.html) method, and terminated after the last
 * [
 * `After`](http://junit.sourceforge.net/javadoc/org/junit/After.html) method.
 */
@JvmOverloads constructor(activityClass: Class<A>, private val fragmentClass: Class<F>, initialTouchMode: Boolean = false, launchActivity: Boolean = true, private val launchFragment: Boolean = true) : ActivityTestRule<A>(activityClass, initialTouchMode, launchActivity) {
    private var fragment: F? = null

    override fun afterActivityLaunched() {
        if (launchFragment) {
            launchFragment(createFragment())
        }
    }

    /**
     * Launches the Fragment under test.
     *
     *
     * Don't call this method directly, unless you explicitly requested not to lazily launch the
     * Fragment manually using the launchFragment flag in
     * [FragmentTestRule.FragmentTestRule].
     *
     *
     * Usage:
     * <pre>
     * &#064;Test
     * public void customIntentToStartActivity() {
     * Fragment fragment = MyFragment.newInstance();
     * fragmentRule.launchFragment(fragment);
     * }
    </pre> *
     *
     * @param fragment The Fragment under test. If `fragment` is null, the fragment returned
     * by [FragmentTestRule.createFragment] is used.
     * @see FragmentTestRule.createFragment
     */
    fun launchFragment(fragment: F?) {
        try {
            runOnUiThread {
                val fragment2 = fragment ?: createFragment()
                this@FragmentTestRule.fragment = fragment2
                activity.supportFragmentManager
                        .beginTransaction()
                        .replace(android.R.id.content, fragment2)
                        .commitNow()
            }
        } catch (throwable: Throwable) {
            throw RuntimeException(throwable)
        }

    }

    /**
     * Override this method to set up the desired Fragment.
     *
     *
     * The default Fragment (if this method returns null or is not overwritten) is:
     * fragmentClass.newInstance()
     *
     * @return the fragment to test.
     */
    protected fun createFragment(): F {
        try {
            return fragmentClass.newInstance()
        } catch (e: InstantiationException) {
            throw AssertionError(String.format("%s: Could not insert %s into %s: %s",
                    javaClass.simpleName,
                    fragmentClass.simpleName,
                    activity.javaClass.simpleName,
                    e.message))
        } catch (e: IllegalAccessException) {
            throw AssertionError(String.format("%s: Could not insert %s into %s: %s",
                    javaClass.simpleName,
                    fragmentClass.simpleName,
                    activity.javaClass.simpleName,
                    e.message))
        }

    }

    /**
     * @return The fragment under test.
     */
    fun getFragment(): F? {
        if (fragment == null) {
            Log.w(TAG, "Fragment wasn't created yet")
        }
        return fragment
    }

    companion object {
        private val TAG = "FragmentTestRule"

        /**
         * Creates an [FragmentTestRule] for the Fragment under test.
         *
         *
         * This factory uses a standard Activity to launch the fragment. If you need a special Activity
         * use the constructors.
         *
         * @param fragmentClass The fragment under test
         * @return the fragmentTestRule
         * @see FragmentTestRule.FragmentTestRule
         */
        fun <F : Fragment> create(fragmentClass: Class<F>): FragmentTestRule<*, F> {
            return FragmentTestRule(FragmentTestActivity::class.java, fragmentClass)
        }

        /**
         * Creates an [FragmentTestRule] for the Fragment under test.
         *
         *
         * This factory uses a standard Activity to launch the fragment. If you need a special Activity
         * use the constructors.
         *
         * @param fragmentClass    The fragment under test
         * @param initialTouchMode true if the Activity should be placed into "touch mode" when started
         * @return the fragmentTestRule
         * @see FragmentTestRule.FragmentTestRule
         */
        fun <F : Fragment> create(fragmentClass: Class<F>, initialTouchMode: Boolean): FragmentTestRule<*, F> {
            return FragmentTestRule(FragmentTestActivity::class.java, fragmentClass, initialTouchMode)
        }

        /**
         * Creates an [FragmentTestRule] for the Fragment under test.
         *
         *
         * This factory uses a standard Activity to launch the fragment. If you need a special Activity
         * use the constructors.
         *
         * @param fragmentClass    The fragment under test
         * @param initialTouchMode true if the Activity should be placed into "touch mode" when started
         * @param launchFragment   true if the Fragment should be launched once per
         * [
 * `Test`](http://junit.org/javadoc/latest/org/junit/Test.html) method. It will be launched before the first
         * [
 * `Before`](http://junit.sourceforge.net/javadoc/org/junit/Before.html) method, and terminated after the last
         * [
 * `After`](http://junit.sourceforge.net/javadoc/org/junit/After.html) method.
         * @return the fragmentTestRule
         * @see FragmentTestRule.FragmentTestRule
         */
        fun <F : Fragment> create(fragmentClass: Class<F>, initialTouchMode: Boolean, launchFragment: Boolean): FragmentTestRule<*, F> {
            return FragmentTestRule(FragmentTestActivity::class.java, fragmentClass, initialTouchMode, true, launchFragment)
        }
    }
}