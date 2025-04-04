/*
 * Copyright The WildFly Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.wildfly.persistence.jipijapa.hibernate7.management;


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.jipijapa.management.spi.EntityManagerFactoryAccess;
import org.jipijapa.management.spi.Operation;
import org.jipijapa.management.spi.PathAddress;

/**
 * Hibernate entity statistics
 *
 * @author Scott Marlow
 */
public class HibernateEntityStatistics extends HibernateAbstractStatistics {

    public static final String OPERATION_ENTITY_DELETE_COUNT = "entity-delete-count";
    public static final String OPERATION_ENTITY_INSERT_COUNT = "entity-insert-count";
    public static final String OPERATION_ENTITY_LOAD_COUNT = "entity-load-count";
    public static final String OPERATION_ENTITY_FETCH_COUNT = "entity-fetch-count";
    public static final String OPERATION_ENTITY_UPDATE_COUNT = "entity-update-count";
    public static final String OPERATION_OPTIMISTIC_FAILURE_COUNT = "optimistic-failure-count";

    public HibernateEntityStatistics() {
        /**
         * specify the different operations
         */
        operations.put(OPERATION_ENTITY_DELETE_COUNT, entityDeleteCount);
        types.put(OPERATION_ENTITY_DELETE_COUNT, Long.class);

        operations.put(OPERATION_ENTITY_INSERT_COUNT, entityInsertCount);
        types.put(OPERATION_ENTITY_INSERT_COUNT, Long.class);

        operations.put(OPERATION_ENTITY_LOAD_COUNT, entityLoadCount);
        types.put(OPERATION_ENTITY_LOAD_COUNT, Long.class);

        operations.put(OPERATION_ENTITY_FETCH_COUNT, entityFetchCount);
        types.put(OPERATION_ENTITY_FETCH_COUNT, Long.class);

        operations.put(OPERATION_ENTITY_UPDATE_COUNT, entityUpdateCount);
        types.put(OPERATION_ENTITY_UPDATE_COUNT, Long.class);

        operations.put(OPERATION_OPTIMISTIC_FAILURE_COUNT, optimisticFailureCount);
        types.put(OPERATION_OPTIMISTIC_FAILURE_COUNT, Long.class);
    }

    private org.hibernate.stat.Statistics getBaseStatistics(EntityManagerFactory entityManagerFactory) {
        if (entityManagerFactory == null) {
            return null;
        }
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        if (sessionFactory != null) {
            return sessionFactory.getStatistics();
        }
        return null;
    }

    private org.hibernate.stat.EntityStatistics getStatistics(EntityManagerFactory entityManagerFactory, PathAddress pathAddress) {
        if (entityManagerFactory == null) {
            return null;
        }
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        if (sessionFactory != null) {
            return sessionFactory.getStatistics().getEntityStatistics(pathAddress.getValue(HibernateStatistics.ENTITY));
        }
        return null;
    }

    private Operation entityDeleteCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            org.hibernate.stat.EntityStatistics statistics = getStatistics(getEntityManagerFactory(args), getPathAddress(args));
            return Long.valueOf(statistics != null ? statistics.getDeleteCount() : 0);
        }
    };

    private Operation entityFetchCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            org.hibernate.stat.EntityStatistics statistics = getStatistics(getEntityManagerFactory(args), getPathAddress(args));
            return Long.valueOf(statistics != null ? statistics.getFetchCount() : 0);
        }
    };

    private Operation entityInsertCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            org.hibernate.stat.EntityStatistics statistics = getStatistics(getEntityManagerFactory(args), getPathAddress(args));
            return Long.valueOf(statistics != null ? statistics.getInsertCount() : 0);
        }
    };

    private Operation entityLoadCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            org.hibernate.stat.EntityStatistics statistics = getStatistics(getEntityManagerFactory(args), getPathAddress(args));
            return Long.valueOf(statistics != null ? statistics.getLoadCount() : 0);
        }
    };

    private Operation entityUpdateCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            org.hibernate.stat.EntityStatistics statistics = getStatistics(getEntityManagerFactory(args), getPathAddress(args));
            return Long.valueOf(statistics != null ? statistics.getUpdateCount() : 0);
        }
    };

    private Operation optimisticFailureCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            org.hibernate.stat.EntityStatistics statistics = getStatistics(getEntityManagerFactory(args), getPathAddress(args));
            return Long.valueOf(statistics != null ? statistics.getOptimisticFailureCount() : 0);
        }
    };

    @Override
    public Set<String> getNames() {

        return Collections.unmodifiableSet(operations.keySet());
    }

    @Override
    public Collection<String> getDynamicChildrenNames(EntityManagerFactoryAccess entityManagerFactoryLookup, PathAddress pathAddress) {
        org.hibernate.stat.Statistics statistics = getBaseStatistics(entityManagerFactoryLookup.entityManagerFactory(pathAddress.getValue(HibernateStatistics.PROVIDER_LABEL)));
        return statistics != null ?
            Collections.unmodifiableCollection(Arrays.asList( statistics.getEntityNames())) :
                Collections.EMPTY_LIST;

    }
}
