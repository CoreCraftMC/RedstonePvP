package com.ryderbelserion.redstonepvp.api.builders.gui.objects.components;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Controls what kind of interaction can happen inside a menu.
 *
 * @author SecretX
 */
public enum InteractionComponent {

    PREVENT_ITEM_PLACE,
    PREVENT_ITEM_TAKE,
    PREVENT_ITEM_SWAP,
    PREVENT_ITEM_DROP,
    PREVENT_OTHER_ACTIONS;

    public static final Set<InteractionComponent> VALUES = Collections.unmodifiableSet(EnumSet.allOf(InteractionComponent.class));

}