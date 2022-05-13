package jp.techacademy.yuuwa.takano.apiapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Contacts.SettingsColumns.KEY
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import io.realm.Realm
import jp.techacademy.yuuwa.takano.apiapp.FavoriteShop.Companion.findBy
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.fragment_api.*

class WebViewActivity: AppCompatActivity() {
    companion object {

        val KEY_SHOP = "key_shop"

        fun start(activity: Activity, shop: Shop) {
            activity.startActivity(Intent(activity, WebViewActivity::class.java).putExtra(KEY_SHOP, shop))
        }
    }
    private val viewPagerAdapter by lazy { ViewPagerAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)



        val states = intent.getSerializableExtra(KEY_SHOP) as Shop

        var fvck = findBy(states.id)//お気に入りチェック

        if (states.couponUrls.sp != null) {

            webView.loadUrl(states.couponUrls.sp)//spの場合

        } else {

            webView.loadUrl(states.couponUrls.pc)//pcの場合

        }

        if (fvck != null) {//お気に入り判定
            favorite_cp.visibility = View.VISIBLE
            no_favorite_cp.visibility = View.INVISIBLE


        } else {
            favorite_cp.visibility = View.INVISIBLE
            no_favorite_cp.visibility = View.VISIBLE


        }



        favorite_cp.setOnClickListener {//お気に入り削除
            favorite_cp.visibility = View.INVISIBLE
            no_favorite_cp.visibility = View.VISIBLE

            showConfirmDeleteFavoriteDialog(states.id)

        }
        no_favorite_cp.setOnClickListener {//お気に入り登録
            no_favorite_cp.visibility = View.INVISIBLE
            favorite_cp.visibility = View.VISIBLE
            FavoriteShop.insert(FavoriteShop().apply {
                id = states.id
                name = states.name
                imageUrl = states.logoImage
                address = states.address
                url = if (states.couponUrls.sp.isNotEmpty()) states.couponUrls.sp else states.couponUrls.pc
            })
        }
    }
    private fun showConfirmDeleteFavoriteDialog(id: String) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_favorite_dialog_title)
            .setMessage(R.string.delete_favorite_dialog_message)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                deleteFavorite(id)
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->}
            .create()
            .show()
    }
    private fun deleteFavorite(id: String) {
        FavoriteShop.delete(id)
        //(viewPagerAdapter.fragments[MainActivity.VIEW_PAGER_POSITION_API] as ApiFragment).updateView()
        //(viewPagerAdapter.fragments[MainActivity.VIEW_PAGER_POSITION_FAVORITE] as FavoriteFragment).updateData()
    }

}

