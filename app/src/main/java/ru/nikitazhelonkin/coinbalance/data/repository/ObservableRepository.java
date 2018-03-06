package ru.nikitazhelonkin.coinbalance.data.repository;


import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class ObservableRepository {

    public static class Event {

        public static final int CHANGE = 0;
        public static final int INSERT = 1;
        public static final int DELETE = 2;

        private Class<?> mEventClass;
        private int mEventType;

        public Event(Class<?> clazz, int eventType) {
            mEventClass = clazz;
            mEventType = eventType;
        }

        public Class<?> getEventClass() {
            return mEventClass;
        }

        public int getEventType() {
            return mEventType;
        }
    }

    private static final Subject<Event> SUBJECT = PublishSubject.create();

    public Observable<Event> observe() {
        return SUBJECT;
    }

    public void notifyChange() {
        SUBJECT.onNext(new Event(this.getClass(), Event.CHANGE));
    }

    public void notifyInsert() {
        SUBJECT.onNext(new Event(this.getClass(), Event.INSERT));
    }

    public void notifyDelete() {
        SUBJECT.onNext(new Event(this.getClass(), Event.DELETE));
    }

}
