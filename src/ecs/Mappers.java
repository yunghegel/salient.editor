package ecs;

import com.badlogic.ashley.core.ComponentMapper;
import ecs.components.ModelComponent;
import ecs.components.PhysicsComponent;
import ecs.components.PickableModelComponent;
import ecs.components.TransformComponent;
import scene.picking.GameObjectPicker;

public class Mappers {

    public final static ComponentMapper<PickableModelComponent> pickableModelComponentComponentMapper = ComponentMapper.getFor(PickableModelComponent.class);
    public final static ComponentMapper<TransformComponent> transformComponentComponentMapper = ComponentMapper.getFor(TransformComponent.class);
    public final static ComponentMapper<ModelComponent> modelComponentComponentMapper = ComponentMapper.getFor(ModelComponent.class);
    public final static ComponentMapper<PhysicsComponent> physicsComponentComponentMapper = ComponentMapper.getFor(PhysicsComponent.class);

}
