package org.ufla.dcc.jsdepextractor.typeinference;

import java.util.Observable;
import java.util.Observer;
import java.util.PriorityQueue;
import org.ufla.dcc.jsdepextractor.project.NodeJsProject;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * Representa a fila de prioridades para inferir tipos em um projeto NodeJs.
 * 
 * Apenas projetos em que todas suas dependências inferiram seus tipos poderão inferir seus
 * respectivos tipos.
 * 
 * Esta representação utiliza o padrão de projeto Observer para notificar aos projetos quando alguma
 * de suas dependências já inferiu seus tipo e então automaticamente remove e inseri novamente o
 * projeto na fila de prioridades para que sua posição seja atualizada.
 */
public class ProjectQueueTypeInference extends Observable
    implements Observer, Comparable<ProjectQueueTypeInference> {

  private NodeJsProject project;

  private int unresolveTypeProjects;

  private PriorityQueue<ProjectQueueTypeInference> projectsQueueForTypeInference;


  /**
   * Constrói uma fila de prioridades para inferir os tipos de todos os projetos que dependem de um
   * projeto NodeJs e também para inferir os tipos deste projeto.
   * 
   * @param project projeto em qual será construída a fila para inferência de tipos
   */
  public ProjectQueueTypeInference(NodeJsProject project) {
    this(project, new PriorityQueue<>(), HashBiMap.create());
  }

  /**
   * Constrói um projeto para inferência de tipos a partir do projeto, da fila de prioridades que
   * irá manter os projetos que serão inferidos os tipos e do mapeamento de instâncias de
   * ProjectQueueTypeInference para suas respectivas de NodeJsProject.
   * 
   * @param project projeto que irá inferir tipos
   * @param projectsQueueForTypeInference fila de prioridades que irá manter os projetos que serão
   *        inferidos os tipos
   * @param mapProjects
   */
  private ProjectQueueTypeInference(NodeJsProject project,
      PriorityQueue<ProjectQueueTypeInference> projectsQueueForTypeInference,
      BiMap<ProjectQueueTypeInference, NodeJsProject> mapProjects) {
    this.project = project;
    this.projectsQueueForTypeInference = projectsQueueForTypeInference;
    this.unresolveTypeProjects = project.getDependencies().size();
    this.projectsQueueForTypeInference.add(this);
    mapProjects.put(this, project);
    createAndAssociateProjectsDependency(mapProjects);
  }

  @Override
  public int compareTo(ProjectQueueTypeInference other) {
    return unresolveTypeProjects - other.unresolveTypeProjects;
  }

  /**
   * Recupera o respectivo projeto NodeJs.
   * 
   * @return respectivo projeto NodeJs
   */
  public NodeJsProject getProject() {
    return project;
  }

  @Override
  public void notifyObservers() {
    super.setChanged();
    super.notifyObservers();
    deleteObservers();
  }

  /**
   * Remove o próximo projeto em que a inferência de tipos deverá ser realizada.
   * 
   * @return próximo projeto em que a inferência de tipos deverá ser realizada
   */
  public NodeJsProject poll() {
    if (projectsQueueForTypeInference.peek() == null) {
      return null;
    }
    projectsQueueForTypeInference.peek().notifyObservers();
    return projectsQueueForTypeInference.poll().project;
  }

  @Override
  public void update(Observable observable, Object arg) {
    unresolveTypeProjects--;
    projectsQueueForTypeInference.remove(this);
    projectsQueueForTypeInference.add(this);
  }

  /**
   * Cria e associa os projetos com que esse projeto mantém dependência.
   * 
   * @param mapProjects mapeamento de instâncias de ProjectQueueTypeInference para suas respectivas
   *        de NodeJsProject
   */
  private void createAndAssociateProjectsDependency(
      BiMap<ProjectQueueTypeInference, NodeJsProject> mapProjects) {
    for (NodeJsProject projectDependency : project.getDependencies()) {
      ProjectQueueTypeInference projectDependencyForTypeInference = null;
      if (mapProjects.containsValue(projectDependency)) {
        projectDependencyForTypeInference = mapProjects.inverse().get(projectDependency);
      } else {
        projectDependencyForTypeInference = new ProjectQueueTypeInference(projectDependency,
            projectsQueueForTypeInference, mapProjects);
      }
      projectDependencyForTypeInference.addObserver(this);
    }
  }

  /**
   * Verifica se a fila está vazia.
   * 
   * @return true, se a fila está vazia, caso contrário, false
   */
  public boolean isEmpty() {
    return projectsQueueForTypeInference.isEmpty();
  }

}
