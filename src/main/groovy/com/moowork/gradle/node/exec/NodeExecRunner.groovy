package com.moowork.gradle.node.exec

import org.gradle.api.Project
import org.gradle.process.ExecResult

class NodeExecRunner
    extends ExecRunner
{
    NodeExecRunner( final Project project )
    {
        super( project )
    }

    @Override
    protected ExecResult doExecute()
    {
        def exec = 'node'
        if ( ext.download )
        {
            def separator = File.pathSeparator
            def nodeBinDir = variant.nodeBinDir.absolutePath
            def nodeModulesBinDir = ext.nodeModulesDir.absolutePath + '/node_modules/.bin'

            def path = nodeBinDir + separator + nodeModulesBinDir

            // Take care of Windows environments that may contain "Path" OR "PATH" - both existing
            // possibly (but not in parallel as of now)
            if ( environment['Path'] != null )
            {
                environment.put('Path', path + separator + environment['Path'])
            }
            else
            {
                environment.put('PATH', path + separator + environment['PATH'])
            }

            exec = variant.nodeExec
        }

        return run( exec, arguments )
    }
}
