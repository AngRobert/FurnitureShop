CREATE TABLE IF NOT EXISTS PODCASTS (
    podcast_id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(500) NOT NULL,
    length INT NOT NULL CHECK (length > 0),
    streams INT NOT NULL DEFAULT 0 CHECK (streams >= 0)
);

CREATE TABLE IF NOT EXISTS CREATORS (
    creator_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS HOSTS (
    creator_id INT PRIMARY KEY,
    CONSTRAINT host_fk FOREIGN KEY (creator_id) REFERENCES CREATORS(creator_id) ON DELETE CASCADE,
    recommended_podcast VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS ARTISTS (
    creator_id INT PRIMARY KEY,
    CONSTRAINT artist_fk FOREIGN KEY (creator_id) REFERENCES CREATORS(creator_id) ON DELETE CASCADE,
    recommended_song VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS PODCAST_HOSTS (
    hosting_id SERIAL PRIMARY KEY,
    creator_id INT NOT NULL,
    podcast_id INT NOT NULL,
    CONSTRAINT podcast_hosts_creator_id_fk FOREIGN KEY (creator_id) REFERENCES CREATORS(creator_id) ON DELETE CASCADE,
    CONSTRAINT podcast_hosts_podcast_id_fk FOREIGN KEY (podcast_id) REFERENCES PODCASTS(podcast_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS TAGS (
    tag_id SERIAL PRIMARY KEY,
    description VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS CREATOR_TAGS (
    creator_tag_id SERIAL PRIMARY KEY,
    tag_id INT NOT NULL,
    creator_id INT NOT NULL,
    CONSTRAINT creator_tags_tag_id_fk FOREIGN KEY (tag_id) REFERENCES TAGS(tag_id) ON DELETE CASCADE,
    CONSTRAINT creator_tags_creator_id_fk FOREIGN KEY (creator_id) REFERENCES CREATORS(creator_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ALBUMS (
    album_id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    release_date DATE NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE IF NOT EXISTS ALBUM_ARTISTS (
    creation_id SERIAL PRIMARY KEY,
    album_id INT NOT NULL,
    creator_id INT NOT NULL,
    CONSTRAINT album_artists_album_id_fk FOREIGN KEY (album_id) REFERENCES ALBUMS(album_id) ON DELETE CASCADE,
    CONSTRAINT album_artists_creator_id_fk FOREIGN KEY (creator_id) REFERENCES ARTISTS(creator_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS SONGS (
    song_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    length INT NOT NULL CHECK (length > 0),
    streams INT NOT NULL DEFAULT 0 CHECK (streams >= 0),
    release_date DATE NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE IF NOT EXISTS ALBUM_TRACKS (
    song_id INT PRIMARY KEY,
    album_id INT NOT NULL,
    track_number INT NOT NULL,
    CONSTRAINT album_track_song_fk FOREIGN KEY (song_id) REFERENCES SONGS(song_id) ON DELETE CASCADE,
    CONSTRAINT album_track_album_fk FOREIGN KEY (album_id) REFERENCES ALBUMS(album_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS SINGLES (
    song_id INT PRIMARY KEY,
    CONSTRAINT single_fk FOREIGN KEY (song_id) REFERENCES SONGS(song_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS SONG_ARTISTS (
    song_artist_id SERIAL PRIMARY KEY,
    song_id INT NOT NULL,
    creator_id INT NOT NULL,
    CONSTRAINT singles_artists_song_id_fk FOREIGN KEY (song_id) REFERENCES SONGS(song_id) ON DELETE CASCADE,
    CONSTRAINT singles_artists_creator_id_fk FOREIGN KEY (creator_id) REFERENCES ARTISTS(creator_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS COLLABORATORS (
    collaborator_id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS SONG_COLLABORATIONS (
    collaboration_id SERIAL PRIMARY KEY,
    collaborator_id INT NOT NULL,
    song_id INT NOT NULL,
    CONSTRAINT song_collaborations_collaborator_id_fk FOREIGN KEY (collaborator_id) REFERENCES COLLABORATORS(collaborator_id) ON DELETE CASCADE,
    CONSTRAINT song_collaborations_song_id_fk FOREIGN KEY (song_id) REFERENCES SONGS(song_id) ON DELETE CASCADE
);