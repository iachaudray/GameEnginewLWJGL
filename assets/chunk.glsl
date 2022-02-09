#type vertex
#version 410
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 normal;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform mat3 normalMatrix;
out vec3 fragNormal;

void main() {
    gl_Position = (projectionMatrix * viewMatrix * modelMatrix) *   vec4(aPos, 1.0);
    fragNormal = normal;
}

#type fragment
#version 410
in vec3 fragNormal;
out vec4 color;
uniform vec3 sunLightDirection;
void main() {
    float ambientStrength = 0.3;
    float x = max(dot(fragNormal, sunLightDirection), 0);
    vec3 light = (ambientStrength + x) * vec3(1.0, 0.0, 0.0);
    color = vec4(light, 1.0);

}