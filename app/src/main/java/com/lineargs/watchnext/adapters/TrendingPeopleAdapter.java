package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import androidx.annotation.NonNull;
import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.ServiceUtils;
import com.lineargs.watchnext.utils.retrofit.people.Person;

import java.util.List;

public class TrendingPeopleAdapter extends BaseTabbedAdapter {

    private List<Person> people;

    public TrendingPeopleAdapter(@NonNull Context context, @NonNull OnItemClickListener listener) {
        super(context, listener);
    }

    @Override
    protected void bindViews(TabbedViewHolder holder, Context context, int position) {
        final Person person = people.get(position);
        holder.title.setText(person.getName());
        holder.star.setVisibility(View.GONE);
        ServiceUtils.loadPicasso(holder.poster.getContext(), "https://image.tmdb.org/t/p/w500/" + person.getProfilePath())
                .resizeDimen(R.dimen.movie_poster_width_default, R.dimen.movie_poster_height_default)
                .centerCrop()
                .placeholder(R.drawable.icon_person_grey)
                .error(R.drawable.icon_person_grey)
                .into(holder.poster);
    }

    @Override
    protected void onViewClick(View view, int position) {
        Person person = people.get(position);
        Uri uri = DataContract.Person.buildPersonUriWithId(person.getId());
        callback.onItemSelected(uri);
    }

    @Override
    public int getItemCount() {
        if (people == null) {
            return 0;
        } else {
            return people.size();
        }
    }

    public void swapPeople(List<Person> people) {
        this.people = people;
        notifyDataSetChanged();
    }
}
