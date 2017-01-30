package com.github.mobile.core.code;

import android.text.TextUtils;
import android.view.View;

import com.github.mobile.R;
import com.github.mobile.core.commit.CommitUtils;
import com.github.mobile.core.ref.RefUtils;
import com.github.mobile.ui.StyledText;
import com.github.mobile.util.TypefaceUtils;

import java.util.Map;
import java.util.TreeMap;

import static org.eclipse.egit.github.core.TreeEntry.TYPE_BLOB;
import static org.eclipse.egit.github.core.TreeEntry.TYPE_TREE;

public class TreeFolder {
    /**
     * Folder in a tree
     */
    public static class Folder implements TreeEntry.Entry {
        /**
         * Parent folder
         */
        public Folder parent;

        /**
         * Raw tree entry
         */
        public org.eclipse.egit.github.core.TreeEntry entry;

        /**
         * Name
         */
        public String name;
        /**
         * Sub folders
         */
        public final Map<String, Folder> folders = new TreeMap<String, Folder>();

        /**
         * Files
         */
        public final Map<String, TreeEntry.Entry> files = new TreeMap<String, TreeEntry.Entry>();

        public Folder() {
            this.parent = null;
            this.entry = null;
            this.name = null;
        }

        public Folder(org.eclipse.egit.github.core.TreeEntry entry, Folder parent) {
            this.entry = entry;
            this.parent = parent;
            this.name = CommitUtils.getName(entry.getPath());
        }

        public void addFile(org.eclipse.egit.github.core.TreeEntry entry, String[] pathSegments, int index) {
            if (index == pathSegments.length - 1) {
                TreeEntry.Entry file = new Folder(entry, this);
                files.put(file.getName(), file);
            } else {
                Folder folder = folders.get(pathSegments[index]);
                if (folder != null)
                    folder.addFile(entry, pathSegments, index + 1);
            }
        }

        public void addFolder(org.eclipse.egit.github.core.TreeEntry entry, String[] pathSegments, int index) {
            if (index == pathSegments.length - 1) {
                Folder folder = new Folder(entry, this);
                folders.put(folder.name, folder);
            } else {
                Folder folder = folders.get(pathSegments[index]);
                if (folder != null)
                    folder.addFolder(entry, pathSegments, index + 1);
            }
        }

        @Override
        public org.eclipse.egit.github.core.TreeEntry getEntry() {
            return entry;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public void add(final org.eclipse.egit.github.core.TreeEntry entry) {
            String type = entry.getType();
            String path = entry.getPath();
            if (TextUtils.isEmpty(path))
                return;

            if (TYPE_BLOB.equals(type)) {
                String[] segments = path.split("/");
                if (segments.length > 1) {
                    Folder folder = folders.get(segments[0]);
                    if (folder != null)
                        folder.addFile(entry, segments, 1);
                } else if (segments.length == 1) {
                    TreeEntry.Entry file = new Folder(entry, this);
                    files.put(file.getName(), file);
                }
            } else if (TYPE_TREE.equals(type)) {
                String[] segments = path.split("/");
                if (segments.length > 1) {
                    Folder folder = folders.get(segments[0]);
                    if (folder != null)
                        folder.addFolder(entry, segments, 1);
                } else if (segments.length == 1) {
                    Folder folder = new Folder(entry, this);
                    folders.put(folder.name, folder);
                }
            }
        }

        @Override
        public boolean isFolder() {
            return true;
        }

        @Override
        public int compareTo(TreeEntry.Entry entry) {
            return name.compareTo(entry.getName());
        }

        @Override
        public String getPath() {
            return entry.getPath();
        }

        @Override
        public String getSha() {
            return entry.getSha();
        }
    }
}
