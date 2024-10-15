package com.example.demo.auth.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPermission is a Querydsl query type for Permission
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPermission extends EntityPathBase<Permission> {

    private static final long serialVersionUID = 646918688L;

    public static final QPermission permission = new QPermission("permission");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath permissionName = createString("permissionName");

    public final SetPath<Role, QRole> roles = this.<Role, QRole>createSet("roles", Role.class, QRole.class, PathInits.DIRECT2);

    public QPermission(String variable) {
        super(Permission.class, forVariable(variable));
    }

    public QPermission(Path<? extends Permission> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPermission(PathMetadata metadata) {
        super(Permission.class, metadata);
    }

}

