package proto.com.bills.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import proto.com.bills.R;
import proto.com.bills.base.BaseActivity;
import proto.com.bills.fragments.CreateGroupDialogFragment;
import proto.com.bills.models.Group;
import proto.com.bills.models.Member;
import proto.com.bills.utils.StackBlurTransformation;

public class MainActivity extends BaseActivity {

    @BindView(R.id.dl_side_menu)
    DrawerLayout dl_side_menu;

    @BindView(R.id.nv_side_menu)
    NavigationView nv_side_menu;

    @BindView(R.id.tb_header)
    Toolbar tb_header;

    @BindView(R.id.rv_groups)
    RecyclerView rv_groups;

    private Unbinder unbinder;
    private ProgressBar pb_load_image;
    private ImageView iv_blur_background;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private CircleImageView civ_profile_pic;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();
        initNavigationDrawer();
    }

    public void initNavigationDrawer() {
        setSupportActionBar(tb_header);
        getSupportActionBar().setTitle("My Groups");

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, dl_side_menu, tb_header,
                R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        dl_side_menu.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        initSideDrawerMenu();
    }

    private void initSideDrawerMenu() {
        final View view = getLayoutInflater().inflate(R.layout.navigation_header, null, false);
        civ_profile_pic = (CircleImageView) view.findViewById(R.id.civ_profile_pic);
        pb_load_image = (ProgressBar)view.findViewById(R.id.pb_load_image);
        iv_blur_background = (ImageView)view.findViewById(R.id.iv_blur_background);
        final TextView tv_full_name = (TextView)view.findViewById(R.id.tv_full_name);

        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        tv_full_name.setText(firebaseUser.getDisplayName());

        Picasso.with(this)
                .load(firebaseUser.getPhotoUrl())
                .transform(new StackBlurTransformation(this, 25, 1))
                .into(iv_blur_background);

        Picasso.with(this)
                .load(firebaseUser.getPhotoUrl())
                .fit()
                .into(civ_profile_pic, new Callback() {
                    @Override
                    public void onSuccess() {
                        pb_load_image.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        pb_load_image.setVisibility(View.GONE);
                    }
                });

        final int screenHeight = getScreenDimension("height");
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                ((int)(screenHeight * .4)));
        
        nv_side_menu.addHeaderView(view);
        nv_side_menu.inflateMenu(R.menu.menu_side_drawer);
        nv_side_menu.getHeaderView(0).setLayoutParams(params);

        nv_side_menu.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_change_password:
                    break;
                case R.id.menu_logout:
                    break;
            }
            dl_side_menu.closeDrawers();
            return true;
        });
    }

    @OnClick(R.id.fab_add_group)
    public void createNewGroup() {
        final CreateGroupDialogFragment fragment = CreateGroupDialogFragment.newInstance();
        fragment.setOnCreateGroupListener((groupName, description) -> {
            fragment.dismiss();
            final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            final Group group = new Group();
            final String uuid = UUID.randomUUID().toString();

            group.setUuid(uuid);
            group.setCreatedBy(new Member(firebaseUser.getUid(), firebaseUser.getDisplayName()));
            group.setGroupName(groupName);
            group.setGroupDescription(description);

            showProgressDialog("Create Group", "Creating new group, Please wait...");
            FirebaseDatabase.getInstance().getReference("groups").child(uuid).setValue(group)
                    .addOnCompleteListener(task -> {
                        dismissProgressDialog();
                        if (task.isSuccessful()) {
                            showConfirmDialog(null, "Create Group", "New group successfully created!", "Close", null, null, true);
                        } else {
                            if (task.getException() != null) {
                                showConfirmDialog(null, "Create Group Failed", task.getException().getMessage(), "Close", null, null, true);
                            }
                        }
                    });
        });
        fragment.show(getFragmentManager(), "create group");
    }

    @Override
    protected void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }
}
