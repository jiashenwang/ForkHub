package com.github.mobile.core.code;

public class TreeEntry {
    public interface Entry extends Comparable<Entry> {

        public String getName();
        public void add(final org.eclipse.egit.github.core.TreeEntry entry);

        public org.eclipse.egit.github.core.TreeEntry getEntry();
        public String getPath();
        public String getSha();

        public boolean isFolder();
//        /**
//         * Parent folder
//         */
//        public final TreeFolder.Folder parent;
//
//        /**
//         * Raw tree entry
//         */
//        public final org.eclipse.egit.github.core.TreeEntry entry;
//
//        /**
//         * Name
//         */
//        public final String name;

//        public Entry() {
//            this.parent = null;
//            this.entry = null;
//            this.name = null;
//        }
//
//        public Entry(org.eclipse.egit.github.core.TreeEntry entry, TreeFolder.Folder parent) {
//            this.entry = entry;
//            this.parent = parent;
//            this.name = CommitUtils.getName(entry.getPath());
//        }

//        @Override
//        public int compareTo(Entry another) {
//            return name.compareTo(another.name);
//        }
    }
}
