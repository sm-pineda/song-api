package com.pineda.song;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pineda/songs")
@CrossOrigin(origins = "*") // <--- ADD THIS LINE HERE
public class SongController {

    @Autowired
    private SongRepository songRepository;

    @GetMapping
    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable Long id) {
        Optional<Song> song = songRepository.findById(id);
        return song.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Song createSong(@RequestBody Song song) {
        return songRepository.save(song);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Song> updateSong(@PathVariable Long id, @RequestBody Song songDetails) {
        Optional<Song> optionalSong = songRepository.findById(id);

        if (!optionalSong.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Song song = optionalSong.get();
        song.setTitle(songDetails.getTitle());
        song.setArtist(songDetails.getArtist());
        song.setAlbum(songDetails.getAlbum());
        song.setGenre(songDetails.getGenre());
        song.setUrl(songDetails.getUrl());

        Song updatedSong = songRepository.save(song);
        return ResponseEntity.ok(updatedSong);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        Optional<Song> song = songRepository.findById(id);
        if (!song.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        songRepository.delete(song.get());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/{query}")
    public List<Song> searchSongs(@PathVariable String query) {
        return songRepository.findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCaseOrAlbumContainingIgnoreCaseOrGenreContainingIgnoreCase(
                query, query, query, query
        );
    }
}