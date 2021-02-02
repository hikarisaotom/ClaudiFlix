package com.example.clauditter.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clauditter.R
import com.example.clauditter.ui.clases.Review


class ReviewHolder(view: View): RecyclerView.ViewHolder(view){
    val thumbnail: ImageView =view.findViewById(R.id.img_author)
    val authorName: TextView =view.findViewById(R.id.lbl_authorName)
    val content: TextView =view.findViewById(R.id.txt_content)
    val puntuation: TextView =view.findViewById(R.id.lbl_puntuation_review)
}

class ReviewAdapter(private var reviews:List<Review>):
    RecyclerView.Adapter<ReviewHolder>() {
    private val TAG="ReviewPreviewAdapter"


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.review_details,parent,false)
        return ReviewHolder(view)
    }

    override fun getItemCount(): Int {
        return if(reviews.isNotEmpty()) reviews.size else 1
    }

    fun loadNewData(newReviews:List<Review>){
        reviews=newReviews
        notifyDataSetChanged()
    }

    fun getReview(position:Int):Review?{
        return if(reviews.isNotEmpty())reviews[position] else null
    }

    override fun onBindViewHolder(holder: ReviewHolder, position: Int) {
        if(reviews.isEmpty()){
            //TODO IMPLEMENT THUMBNAIL
           holder.authorName.text="John Doe"
            holder.content.text="Not avaliable"
        }else{
            val review=reviews[position]
            Glide.with(holder.itemView)  //2
                .load(review.authorDetails.avatar_path) //3
                .into(holder.thumbnail) //8
            holder.authorName.text=review.authorDetails.username
            holder.content.text="PENDIENTE"
            if(review.authorDetails.rating!=null){
                holder.puntuation.text=review.authorDetails.rating!!.toString()+" /10"
            }else{
                holder.puntuation.text="N/A"
            }

        }
    }

}