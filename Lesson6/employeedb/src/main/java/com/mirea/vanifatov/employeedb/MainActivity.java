package com.mirea.vanifatov.employeedb;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Update;

import com.mirea.vanifatov.employeedb.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AppDatabase db;
    private SuperheroDao dao;
    private SuperHeroesAdapter heroAdapter;
    private long selectedHeroId = -1;

    @Entity(tableName = "superheroes")
    public static class Superheroes {
        @PrimaryKey(autoGenerate = true)
        public long id;
        public String name;
        public String surname;
        public String age;
    }

    @Dao
    public interface SuperheroDao {
        @Query("SELECT * FROM Superheroes")
        List<Superheroes> getAll();

        @Query("SELECT * FROM Superheroes WHERE id = :id")
        Superheroes getId(long id);

        @Insert
        void ins(Superheroes superheroes);

        @Update
        void upd(Superheroes superheroes);

        @Delete
        void del(Superheroes superheroes);
    }

    @Database(entities = {Superheroes.class}, version = 3, exportSchema = false)
    public abstract static class AppDatabase extends RoomDatabase {
        public abstract SuperheroDao superheroDao();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try {
            db = Room.databaseBuilder(getApplicationContext(),
                            AppDatabase.class, "superheroes_db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
            dao = db.superheroDao();
        } catch (Exception e) {
            Log.e("DB_ERROR", "Database initialization failed", e);
            finish();
            return;
        }

        setupRecyclerView();
        setupButtons();
    }

    private void setupRecyclerView() {
        heroAdapter = new SuperHeroesAdapter(new ArrayList<>());
        binding.recyclerList.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerList.setAdapter(heroAdapter);
        loadHeroes();
    }

    private void setupButtons() {
        binding.buttonAdd.setOnClickListener(v -> addHero());
        binding.buttonEdit.setOnClickListener(v -> editHero());
        binding.buttonDel.setOnClickListener(v -> deleteHero());
    }

    private void addHero() {
        String name = binding.editTextName.getText().toString().trim();
        String surname = binding.editTextSName.getText().toString().trim();
        String age = binding.editTextAge.getText().toString().trim();

        Superheroes hero = new Superheroes();
        hero.name = name;
        hero.surname = surname;
        hero.age = age;

        dao.ins(hero);
        clearFields();
        loadHeroes();
    }

    private void editHero() {
        String name = binding.editTextName.getText().toString().trim();
        String surname = binding.editTextSName.getText().toString().trim();
        String age = binding.editTextAge.getText().toString().trim();

        Superheroes hero = dao.getId(selectedHeroId);

        hero.name = name;
        hero.surname = surname;
        hero.age = age;

        dao.upd(hero);
        clearFields();
        selectedHeroId = -1;
        loadHeroes();
    }

    private void deleteHero() {

        Superheroes hero = dao.getId(selectedHeroId);
        dao.del(hero);
        clearFields();
        selectedHeroId = -1;
        loadHeroes();
    }

    private void loadHeroes() {
        try {
            List<Superheroes> heroes = dao.getAll();
            heroAdapter.setHeroes(heroes);
        } catch (Exception e) {
            Log.e("DB_ERROR", "Ошибка при загрузке БД", e);
        }
    }

    private void clearFields() {
        binding.editTextName.setText("");
        binding.editTextSName.setText("");
        binding.editTextAge.setText("");
    }

    private class SuperHeroesAdapter extends RecyclerView.Adapter<SuperHeroesAdapter.HeroViewHolder> {
        private List<Superheroes> heroes;

        public SuperHeroesAdapter(List<Superheroes> heroes) {
            this.heroes = heroes != null ? heroes : new ArrayList<>();
        }

        public void setHeroes(List<Superheroes> heroes) {
            this.heroes = heroes != null ? heroes : new ArrayList<>();
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public HeroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
            return new HeroViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HeroViewHolder holder, int position) {
            holder.bind(heroes.get(position));
        }

        @Override
        public int getItemCount() {
            return heroes.size();
        }

        class HeroViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private final android.widget.TextView textView;
            private Superheroes hero;

            public HeroViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(android.R.id.text1);
                itemView.setOnClickListener(this);
            }

            void bind(Superheroes hero) {
                this.hero = hero;
                textView.setText(hero.name + " | " + hero.surname + " | " + hero.age);
            }

            @Override
            public void onClick(View v) {
                selectedHeroId = hero.id;
                binding.editTextName.setText(hero.name);
                binding.editTextSName.setText(hero.surname);
                binding.editTextAge.setText(hero.age);
            }
        }
    }
}